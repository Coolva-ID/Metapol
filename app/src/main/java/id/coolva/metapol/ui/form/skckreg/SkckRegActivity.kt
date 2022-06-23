package id.coolva.metapol.ui.form.skckreg

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.AndroidEntryPoint
import id.coolva.metapol.core.domain.model.SKCKReg
import id.coolva.metapol.databinding.ActivitySkckRegBinding
import id.coolva.metapol.ui.form.simreg.SIMRegActivity
import java.io.File

@AndroidEntryPoint
class SkckRegActivity : AppCompatActivity() {

    private val binding: ActivitySkckRegBinding by lazy {
        ActivitySkckRegBinding.inflate(layoutInflater)
    }

    private val viewModel: SkckRegViewModel by viewModels()

    private var selectedInput = ""
    private var ktpPath: String = ""
    private var akteLahirPath: String = ""
    private var kartuKeluargaPath: String = ""
    private var pasPhotoPath: String = ""
    val mAuth = FirebaseAuth.getInstance()
    val user: FirebaseUser? = mAuth.currentUser
    val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val actionBar = supportActionBar
        actionBar!!.title = "Pendaftaran SKCK"
        actionBar!!.setBackgroundDrawable(ColorDrawable(0xFFFFFFFF.toInt()))
        actionBar!!.elevation = 0F

        binding.btnUploadKtp.setOnClickListener {
            if (allowedReadExternalStorage(this)) {
                selectImage()
                this.selectedInput = KTP
            } else {
                requestReadExternalStorage(this)
            }
        }

        binding.btnUploadAkte.setOnClickListener {
            if (allowedReadExternalStorage(this)) {
                selectImage()
                this.selectedInput = AKTE_LAHIR
            } else {
                requestReadExternalStorage(this)
            }
        }

        binding.btnUploadKartuKeluarga.setOnClickListener {
            if (allowedReadExternalStorage(this)) {
                selectImage()
                this.selectedInput = KARTU_KELUARGA
            } else {
                requestReadExternalStorage(this)
            }
        }

        binding.btnUploadPasPhoto.setOnClickListener {
            if (allowedReadExternalStorage(this)) {
                selectImage()
                this.selectedInput = PAS_PHOTO
            } else {
                requestReadExternalStorage(this)
            }
        }

        binding.btnSubmit.setOnClickListener {
            submitData()
        }
    }

    /**
     * Get PDF File
     */
    private var sActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback<ActivityResult> { result ->
            if (result.resultCode == RESULT_OK) {
                val data: Intent? = result.data
                if (data != null) {
                    // get PDF uri
                    val uri: Uri? = data.data
                    // get PDF path
                    val path = uri?.path
                    // set file path with name of file
                    if (path != null) {
                        val file = File(path)
                        when (this.selectedInput) {
                            KTP -> {
                                val imageExtension = MimeTypeMap.getSingleton()
                                    .getExtensionFromMimeType(
                                        contentResolver.getType(
                                            uri
                                        )
                                    )

                                // upload photo to google storage
                                val sRef: StorageReference =
                                    FirebaseStorage.getInstance().reference.child(
                                        "ktpskck-" + user!!.uid.toString() + "-" + System.currentTimeMillis() + "." + imageExtension
                                    )
                                sRef.putFile(uri)
                                    .addOnSuccessListener { taskSnapshot ->
                                        taskSnapshot.metadata!!.reference!!.downloadUrl
                                            .addOnSuccessListener { url ->
                                                Log.e("ktp", url.toString())
                                                this.ktpPath = url.toString()
                                            }.addOnFailureListener { exception ->
                                                Toast.makeText(
                                                    this,
                                                    exception.message,
                                                    Toast.LENGTH_LONG
                                                ).show()
                                            }
                                    }
                                binding.btnUploadKtp.text = "Terupload"
                                binding.btnUploadKtp.setBackgroundColor(0xFF1dd1a1.toInt())
                            }
                            AKTE_LAHIR -> {
                                val imageExtension = MimeTypeMap.getSingleton()
                                    .getExtensionFromMimeType(
                                        contentResolver.getType(
                                            uri
                                        )
                                    )

                                // upload photo to google storage
                                val sRef: StorageReference =
                                    FirebaseStorage.getInstance().reference.child(
                                        "akteskck-" + user!!.uid.toString() + "-" + System.currentTimeMillis() + "." + imageExtension
                                    )
                                sRef.putFile(uri)
                                    .addOnSuccessListener { taskSnapshot ->
                                        taskSnapshot.metadata!!.reference!!.downloadUrl
                                            .addOnSuccessListener { url ->
                                                Log.e("akte", url.toString())
                                                this.akteLahirPath = url.toString()
                                            }.addOnFailureListener { exception ->
                                                Toast.makeText(
                                                    this,
                                                    exception.message,
                                                    Toast.LENGTH_LONG
                                                ).show()
                                            }
                                    }
                                binding.btnUploadAkte.text = "Terupload"
                                binding.btnUploadAkte.setBackgroundColor(0xFF1dd1a1.toInt())
                            }
                            KARTU_KELUARGA -> {
                                val imageExtension = MimeTypeMap.getSingleton()
                                    .getExtensionFromMimeType(
                                        contentResolver.getType(
                                            uri
                                        )
                                    )

                                // upload photo to google storage
                                val sRef: StorageReference =
                                    FirebaseStorage.getInstance().reference.child(
                                        "kkskck-" + user!!.uid.toString() + "-" + System.currentTimeMillis() + "." + imageExtension
                                    )
                                sRef.putFile(uri)
                                    .addOnSuccessListener { taskSnapshot ->
                                        taskSnapshot.metadata!!.reference!!.downloadUrl
                                            .addOnSuccessListener { url ->
                                                Log.e("ll", url.toString())
                                                this.kartuKeluargaPath = url.toString()
                                            }.addOnFailureListener { exception ->
                                                Toast.makeText(
                                                    this,
                                                    exception.message,
                                                    Toast.LENGTH_LONG
                                                ).show()
                                            }
                                    }
                                binding.btnUploadKartuKeluarga.text = "Terupload"
                                binding.btnUploadKartuKeluarga.setBackgroundColor(0xFF1dd1a1.toInt())
                            }
                            PAS_PHOTO -> {
                                val imageExtension = MimeTypeMap.getSingleton()
                                    .getExtensionFromMimeType(
                                        contentResolver.getType(
                                            uri
                                        )
                                    )

                                // upload photo to google storage
                                val sRef: StorageReference =
                                    FirebaseStorage.getInstance().reference.child(
                                        "ppskck-" + user!!.uid.toString() + "-" + System.currentTimeMillis() + "." + imageExtension
                                    )
                                sRef.putFile(uri)
                                    .addOnSuccessListener { taskSnapshot ->
                                        taskSnapshot.metadata!!.reference!!.downloadUrl
                                            .addOnSuccessListener { url ->
                                                Log.e("ll", url.toString())
                                                this.pasPhotoPath = url.toString()
                                            }.addOnFailureListener { exception ->
                                                Toast.makeText(
                                                    this,
                                                    exception.message,
                                                    Toast.LENGTH_LONG
                                                ).show()
                                            }
                                    }
                                binding.btnUploadPasPhoto.text = "Terupload"
                                binding.btnUploadPasPhoto.setBackgroundColor(0xFF1dd1a1.toInt())
                            }
                        }
                    } else {
                        Toast.makeText(this, "File belum dipilih", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    )

    private fun selectPDF() {
        // Intent.ACTION_OPEN_DOCUMENT or Intent.ACTION_GET_CONTENT
        val data = Intent(Intent.ACTION_OPEN_DOCUMENT)
        data.type = "image/*"
        val intent = Intent.createChooser(data, "Choose a file")
        sActivityResultLauncher.launch(intent)
    }

    private fun selectImage() {
        // Intent.ACTION_OPEN_DOCUMENT or Intent.ACTION_GET_CONTENT
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        val result = Intent.createChooser(galleryIntent, "Pilih foto")
        sActivityResultLauncher.launch(galleryIntent)
    }
    /**
     * SUBMIT DATA
     */
    private fun submitData() {
        var inputValid = true
        binding.apply {
            if (ktpPath.isEmpty() || akteLahirPath.isEmpty() || kartuKeluargaPath.isEmpty() || pasPhotoPath.isEmpty()) {
                inputValid = false
            }

            if (inputValid) {
                val skckReg = hashMapOf(
                    "uid" to user!!.uid,
                    "identityCardPhotoPath" to ktpPath,
                    "aktePhotoPath" to akteLahirPath,
                    "kkPhotoPath" to kartuKeluargaPath,
                    "pasPhotoPath" to pasPhotoPath,
                    "status" to "Menunggu Verifikasi",
                    "pengajuanAt" to System.currentTimeMillis().toString()
                )
                db.collection("skck")
                    .document(user!!.uid)
                    .set(skckReg, SetOptions.merge())
                    .addOnSuccessListener { documentReference ->
                        Log.d(
                            ContentValues.TAG,
                            "DocumentSnapshot successfully written!"
                        )
                    }
                val skckUpdate = hashMapOf(
                    "skck" to 1
                )
                db.collection("users")
                    .document(user!!.uid)
                    .set(skckUpdate, SetOptions.merge())
//                viewModel.insertSKCKReg(skckReg)
                Toast.makeText(
                    this@SkckRegActivity,
                    "Pendaftaran Berhasil Diajukan",
                    Toast.LENGTH_SHORT
                ).show()
                onBackPressed()
                finish()
            } else {
                Toast.makeText(
                    this@SkckRegActivity,
                    "Mohon lengkapi dokumen yang diperlukan.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    /**
     * Checking Permission
     */
    private fun allowedReadExternalStorage(context: Context): Boolean {
        return ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestReadExternalStorage(activity: Activity) {
        // request permission
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            1
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // check condition
        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // DO NOTHING
        } else {
            Toast.makeText(this, "Izin akses file ditolak.", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        val KTP = "ktp"
        val AKTE_LAHIR = "akte_lahir"
        val KARTU_KELUARGA = "kartu_keluarga"
        val PAS_PHOTO = "pas_photo"
    }
}