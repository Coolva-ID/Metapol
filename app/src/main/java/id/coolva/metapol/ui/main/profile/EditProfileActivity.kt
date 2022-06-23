package id.coolva.metapol.ui.main.profile

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import id.coolva.metapol.R
import id.coolva.metapol.core.data.testing.User
import id.coolva.metapol.databinding.ActivityEditProfileBinding
import id.coolva.metapol.databinding.ActivitySkckRegBinding
import id.coolva.metapol.ui.main.MainActivity
import id.coolva.metapol.utils.Constants
import id.coolva.metapol.utils.Preferences

class EditProfileActivity : AppCompatActivity() {

    private val binding: ActivityEditProfileBinding by lazy {
        ActivityEditProfileBinding.inflate(layoutInflater)
    }

    private lateinit var preferences: Preferences
    private val mAuth = FirebaseAuth.getInstance()
    val user: FirebaseUser? = mAuth.currentUser
    private val db = Firebase.firestore
    var mUser: User? = null
    private var fileSelected: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // setup action bar
        val actionBar = supportActionBar
        actionBar!!.title = "Edit Profile"
        actionBar!!.setBackgroundDrawable(ColorDrawable(0xFFFFFFFF.toInt()))
        actionBar!!.elevation = 0F

        // setup preference
        preferences = Preferences(this)


        db.collection("users").document(user!!.uid)
            .get()
            .addOnSuccessListener { document ->
                mUser = document.toObject(User::class.java)!!
                Log.d("Name", mUser?.foto_profil.toString())
                Log.d("Photo", mUser?.foto_profil.toString())
                binding.edtUserEmail.setText(mUser?.email.toString())
                binding.edtUserFullName.setText(mUser?.nama.toString())
                binding.edtUserPhoneNo.setText(mUser?.noHp.toString())
                if (mUser!!.foto_profil.toString() != "") {
                    Glide.with(this)
                        .load(mUser!!.foto_profil.toString())
                        .into(binding.ivProfileImage)
                }
            }
//        loadDataFromPreferences()

        binding.apply {
            ivChangePhoto.setOnClickListener {
                if (ContextCompat.checkSelfPermission(
                        this@EditProfileActivity,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    val galleryIntent = Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    )
                    val result = Intent.createChooser(galleryIntent, "Pilih foto")
                    sActivityResultLauncher.launch(galleryIntent)
                } else {
                    ActivityCompat.requestPermissions(
                        this@EditProfileActivity,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        1
                    )
                }
            }
            btnUpdate.setOnClickListener {
                when {
                    TextUtils.isEmpty(edtUserFullName.text.toString()) -> {
                        edtUserFullName.error = "Anda belum memasukkan Nama"
                    }
                    TextUtils.isEmpty(edtUserEmail.text.toString().trim() { it <= ' ' }) -> {
                        edtUserEmail.error = "Anda belum memasukkan Email"
                    }
                    TextUtils.isEmpty(edtUserPhoneNo.text.toString()) -> {
                        edtUserPhoneNo.error = "Anda belum memasukkan Nomor Telepon"
                    }

                    else -> {
                        val fullName: String = edtUserFullName.text.toString()
                        val email: String = edtUserEmail.text.toString().trim { it <= ' ' }
                        val phoneNumber: String = edtUserPhoneNo.text.toString().trim { it <= ' ' }

                        // TODO: Update data in firebase
                        Log.e("Checkpoint", "sampai firebase")
                        val updateUser = hashMapOf(
                            "nama" to fullName,
                            "email" to email,
                            "noHp" to phoneNumber
                        )

                        user.updateEmail(email)

                        db.collection("users")
                            .document(user!!.uid)
                            .set(updateUser, SetOptions.merge())
                            .addOnSuccessListener {
                                Log.e("Checkpoint", "Berhasil update data")
                                Toast.makeText(
                                    this@EditProfileActivity,
                                    "Profile berhasil diupdate",
                                    Toast.LENGTH_LONG
                                ).show()
                            }


                        // TODO: Update data in Local Preferences
//                        preferences.setValues(Constants.USER_NAME, fullName)
//                        preferences.setValues(Constants.USER_EMAIL, email)
//                        preferences.setValues(Constants.USER_PHONE_NUMBER, phoneNumber)
////                        preferences.setValues(Constants.USER_PHOTO_PATH, profilePhoto)

                        // TODO: Refresh Data
                        startActivity(Intent(this@EditProfileActivity, EditProfileActivity::class.java))
                    }
                }
            }
        }
    }
    private var sActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback<ActivityResult> { result ->
            if (result.resultCode == RESULT_OK) {
                val data: Intent? = result.data
                if (data != null) {
                    fileSelected = data.data!!
                    if (fileSelected != null) {
                        val imageExtension = MimeTypeMap.getSingleton()
                            .getExtensionFromMimeType(
                                contentResolver!!.getType(
                                    fileSelected!!
                                )
                            )

                        // upload photo to google storage
                        val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
                            "pp-" + user!!.uid.toString() + "-" + System.currentTimeMillis() + "." + imageExtension
                        )
                        sRef.putFile(fileSelected!!)
                            .addOnSuccessListener { taskSnapshot ->
                                taskSnapshot.metadata!!.reference!!.downloadUrl
                                    .addOnSuccessListener { url ->
                                        // update image uri in firestore database
                                        val userImageUpdate = hashMapOf(
                                            "foto_profil" to url
                                        )
                                        db.collection("users")
                                            .document(user!!.uid)
                                            .set(userImageUpdate, SetOptions.merge())
                                            .addOnSuccessListener { documentReference ->
                                                Log.d(
                                                    ContentValues.TAG,
                                                    "DocumentSnapshot successfully written!"
                                                )
                                            }
                                        fileSelected = url
                                        Log.e("Photo", url.toString())
                                        Glide.with(this)
                                            .load(mUser!!.foto_profil.toString())
                                            .into(binding.ivProfileImage)
                                    }.addOnFailureListener { exception ->
                                        Toast.makeText(
                                            this,
                                            exception.message,
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                            }

                    } else {
                        Toast.makeText(this, "File belum dipilih", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    )
    private fun loadDataFromPreferences() {
        binding.apply {
            val userName = preferences.getValues(Constants.USER_NAME) ?: ""
            val userEmail = preferences.getValues(Constants.USER_EMAIL) ?: ""
            val userProfilePhoto = preferences.getValues(Constants.USER_PHOTO_PATH) ?: ""

            edtUserFullName.setText(userName)
            edtUserEmail.setText(userEmail)

            if (userProfilePhoto != "") {
                Glide.with(this@EditProfileActivity)
                    .load(userProfilePhoto)
                    .into(ivProfileImage)
            } else {
                ivProfileImage
            }
        }
    }
}