package id.coolva.metapol.ui.main.profile

import android.Manifest
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import id.coolva.metapol.R
import id.coolva.metapol.core.data.testing.User
import id.coolva.metapol.ui.auth.LoginActivity
import id.coolva.metapol.ui.main.MainActivity
import id.coolva.metapol.utils.Constants
import id.coolva.metapol.utils.Preferences
import id.coolva.metapol.databinding.FragmentProfileBinding


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var preferences: Preferences
    private var fileSelected: Uri? = null
    private val mAuth = FirebaseAuth.getInstance()
    val user: FirebaseUser? = mAuth.currentUser
    var mUser: User? = null
    private val db = Firebase.firestore
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // setup preference
//        preferences = Preferences(requireContext())
//
//        loadDataFromPreferences()


        // get profile photo uri from database
        db.collection("users").document(user!!.uid)
            .get()
            .addOnSuccessListener { document ->
                mUser = document.toObject(User::class.java)!!
                Log.d("Name", mUser?.foto_profil.toString())
                Log.d("Photo", mUser?.foto_profil.toString())
                if (mUser!!.foto_profil.toString() != "") {
                    Glide.with(requireContext())
                        .load(mUser!!.foto_profil.toString())
                        .into(binding.profileImage)
                }
            }


//        binding.btnLogout.setOnClickListener {
//            // reset data
//            preferences.setValues(Constants.USER_NAME, null)
//            preferences.setValues(Constants.USER_EMAIL, null)
//            preferences.setValues(Constants.USER_PHONE_NUMBER, null)
////                preferences.setValues(Constants.USER_PHOTO_PATH, user.profilePhoto)
//            preferences.setValues(Constants.USER_LOGIN_STATUS, "0") // if equal to 1, that means user is logged in
//
//            val moveToMain = Intent(requireContext(), LoginActivity::class.java)
//            startActivity(moveToMain)
//            activity?.finish()
//        }

        binding.btnLogout.setOnClickListener {
            Log.e("Keluar", "Kepencet")
            val builder = MaterialAlertDialogBuilder(requireContext())
            builder.setTitle("Keluar Aplikasi")
            builder.setMessage("Apakah yakin ingin keluar?")
                .setCancelable(false)
                .setPositiveButton("Yakin", DialogInterface.OnClickListener { dialogInterface, i ->
                    val alertDialog: AlertDialog = builder.create()
                    alertDialog.show()
                    FirebaseAuth.getInstance().signOut()
                    Log.e("Keluar", "Berhasil")
                    startActivity(Intent(requireContext(), LoginActivity::class.java))
                    activity?.finish()
                })
                .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialogInterface, i ->
                    dialogInterface.cancel()
                })
            val alertDialog: AlertDialog = builder.create()
            alertDialog.show()
//            // reset data on preferences
//            preferences.setValues(Constants.USER_NAME, null)
//            preferences.setValues(Constants.USER_EMAIL, null)
//            preferences.setValues(Constants.USER_PHOTO_PATH, null)
//            preferences.setValues(
//                Constants.USER_LOGIN_STATUS,
//                "0"
//            ) // if equal to 1, that means user is logged in
//
//            // then move back to Login
//            startActivity(Intent(requireContext(), LoginActivity::class.java))
        }

        binding.ivChangePhoto.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
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
                    requireActivity(),
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    1
                )
            }
        }

        binding.cardEditProfile.setOnClickListener {
            startActivity(Intent(requireContext(), EditProfileActivity::class.java))
        }
    }

    private fun loadDataFromPreferences() {
        binding.apply {
            val profilePath = preferences.getValues(Constants.USER_PHOTO_PATH) ?: ""
            Log.e("ProfileFragment: ", profilePath.toString())
            if (profilePath != "") {
                Glide.with(requireContext())
                    .load(profilePath)
                    .into(profileImage)
            }
        }
    }

    private var sActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback<ActivityResult> { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                val data: Intent? = result.data
                if (data != null) {
                    fileSelected = data.data!!
                    if (fileSelected != null) {
                        val imageExtension = MimeTypeMap.getSingleton()
                            .getExtensionFromMimeType(
                                context?.contentResolver!!.getType(
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
                                        Glide.with(requireContext())
                                            .load(mUser!!.foto_profil.toString())
                                            .into(binding.profileImage)
//                                        updateUserPhotoPreferences()
                                    }.addOnFailureListener { exception ->
                                        Toast.makeText(
                                            requireContext(),
                                            exception.message,
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                            }

                    } else {
                        Toast.makeText(requireContext(), "File belum dipilih", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    )

    private fun updateUserPhotoPreferences() {
        var mUser: User? = null

        // load data from firebase
        db.collection("users").document(user!!.uid)
            .get()
            .addOnSuccessListener { document ->
                mUser = document.toObject(User::class.java)!!
                Log.e("ProfileFragment(PhotoFromFirebase):", mUser?.foto_profil.toString())
                if (mUser!!.foto_profil.toString() != "") {
                    // save to preference
                    preferences.setValues(Constants.USER_PHOTO_PATH, mUser?.foto_profil)
                    // refresh data
                    loadDataFromPreferences()
                    Toast.makeText(requireContext(), "Photo Profile berhasil diperbaharui", Toast.LENGTH_SHORT).show()
                }
            }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {

        } else {
            Toast.makeText(
                context,
                "Oops you just denied permisson for storage",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}