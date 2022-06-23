package id.coolva.metapol.ui.form.simreg

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
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
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
import id.coolva.metapol.R
import id.coolva.metapol.core.data.testing.User
import id.coolva.metapol.core.domain.model.SIMRegsitration
import id.coolva.metapol.databinding.ActivitySimRegistrationBinding
import java.io.File


@AndroidEntryPoint
class SIMRegActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivitySimRegistrationBinding.inflate(layoutInflater)
    }

    private val viewModel: SimRegViewModel by viewModels()

    private var fileSelected = ""
    private var ttdPhotoFilePath: String = ""
    private var pasPhotoFilePath: String = ""
    val mAuth = FirebaseAuth.getInstance()
    val user: FirebaseUser? = mAuth.currentUser
    val db = Firebase.firestore
    var mUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val actionBar = supportActionBar
        actionBar!!.title = "Pendaftaran Ujian SIM"
        actionBar.setBackgroundDrawable(ColorDrawable(0xFFFFFFFF.toInt()))
        actionBar.elevation = 0F

        // set Dropdown for Golongan SIM
        val golSim: Array<String> = resources.getStringArray(R.array.golongan_sim)
        val arrayAdapterGolSim: ArrayAdapter<String> =
            ArrayAdapter(this, android.R.layout.simple_list_item_1, golSim)
        binding.actGolSim.setAdapter(arrayAdapterGolSim)

        // set Dropdown for Sertifikat Sekolah Mengemudi
        val sertSim: Array<String> = resources.getStringArray(R.array.sert_mengemudi)
        val arrayAdapterSertSim: ArrayAdapter<String> =
            ArrayAdapter(this, android.R.layout.simple_list_item_1, sertSim)
        binding.actSertMengemudi.setAdapter(arrayAdapterSertSim)

        binding.btnUploadSignature.setOnClickListener {
            if (!allowedReadExternalStorage(this)) {
                requestReadExternalStorage(this)
            } else {
                // when permission is granted
                selectImage()
                this.fileSelected = TTD_PHOTO
            }
        }

        binding.btnUploadPasPhoto.setOnClickListener {
            if (!allowedReadExternalStorage(this)) {
                requestReadExternalStorage(this)
            } else {
                // when permission is granted
                selectImage()
                this.fileSelected = PAS_PHOTO
            }
        }

        binding.btnSubmit.setOnClickListener {
            binding.apply {
                var isNotEmpty = true

                // get data from user input
                val golonganSimValue = actGolSim.text.toString()
                val sertMengemudiValue = actSertMengemudi.text.toString()
                val ttdPhotoValue = ttdPhotoFilePath
                val pasPhotoValue = pasPhotoFilePath
                val contactNameValue = edtContactName.text.toString()
                val contactAddressValue = edtContactAddress.text.toString()
                val contactPhoneValue = edtContactPhone.text.toString()

                /**
                 * CHECKING DATA FROM USER INPUT
                 */
                if (golonganSimValue.isEmpty()) {
                    actGolSim.error = "Anda belum memilih Golongan SIM"
                    isNotEmpty = false
                }
                if (sertMengemudiValue.isEmpty()) {
                    actSertMengemudi.error = "Anda belum mengisi Sertifikat Mengemudi"
                    isNotEmpty = false
                }

                if (ttdPhotoValue.isEmpty()) {
                    Toast.makeText(
                        this@SIMRegActivity,
                        "Anda belum menambahkan Photo TTD",
                        Toast.LENGTH_SHORT
                    ).show()
                    isNotEmpty = false
                }

                if (pasPhotoValue.isEmpty()) {
                    Toast.makeText(
                        this@SIMRegActivity,
                        "Anda belum menambahkan Pas Photo",
                        Toast.LENGTH_SHORT
                    ).show()
                    isNotEmpty = false
                }

                if (contactNameValue.isEmpty()) {
                    edtContactName.error = "Nama belum diisi"
                    isNotEmpty = false
                }
                if (contactAddressValue.isEmpty()) {
                    edtContactAddress.error = "Alamat belum diisi"
                    isNotEmpty = false
                }
                if (contactPhoneValue.isEmpty()) {
                    edtContactPhone.error = "No. Telepon belum diisi"
                    isNotEmpty = false
                }

                /**
                 * IF ALL FIELD ARE NOT EMPTY
                 */
                if (isNotEmpty) {
                    var memilikiSertMengemudi = false
                    if (sertMengemudiValue == "Ada") {
                        memilikiSertMengemudi = true
                    }
                    val simReg = hashMapOf(
                        "uid" to user!!.uid,
                        "golonganSIM" to golonganSimValue,
                        "memilikiSertMengemudi" to memilikiSertMengemudi,
                        "ttd" to ttdPhotoValue,
                        "pasPhoto" to pasPhotoValue,
                        "contactName" to contactNameValue,
                        "contactAddress" to contactAddressValue,
                        "contactPhoneNo" to contactPhoneValue,
                        "status" to "Menunggu Verifikasi"
                    )
                    db.collection("sim")
                        .document(user!!.uid)
                        .set(simReg, SetOptions.merge())
                        .addOnSuccessListener { documentReference ->
                            Log.d(
                                ContentValues.TAG,
                                "DocumentSnapshot successfully written!"
                            )
                        }
                    val simUpdate = hashMapOf(
                        "sim" to 1
                    )
                    db.collection("users")
                        .document(user!!.uid)
                        .set(simUpdate, SetOptions.merge())
//                    viewModel.insertSIMRegistration(simReg)
                    Toast.makeText(
                        this@SIMRegActivity,
                        "Pendaftaran Berhasil Diajukan",
                        Toast.LENGTH_SHORT
                    ).show()
                    onBackPressed()
                }
            }
        }
    }


    // launcher to get image File
    private var sActivityResultLauncher = registerForActivityResult(
        StartActivityForResult(),
        ActivityResultCallback<ActivityResult> { result ->
            if (result.resultCode == RESULT_OK) {
                val data: Intent? = result.data
                if (data != null) {
                    // get PDF uri
                    val uri: Uri? = data.data
                    Log.e("DAFTAR SIM URI: ", uri.toString())

                    // get PDF path
                    val path = uri?.path
                    Log.e("DAFTAR SIM PATH: ", path.toString())
                    // set file path with name of file
                    if (path != null) {
                        val file = File(path)
                        Log.e("DAFTAR SIM NAMA FILE: ", file.name)

                        if (this.fileSelected == PAS_PHOTO) {
                            val imageExtension = MimeTypeMap.getSingleton()
                                .getExtensionFromMimeType(
                                    contentResolver.getType(
                                        uri
                                    )
                                )

                            // upload photo to google storage
                            val sRef: StorageReference =
                                FirebaseStorage.getInstance().reference.child(
                                    "ppsim-" + user!!.uid.toString() + "-" + System.currentTimeMillis() + "." + imageExtension
                                )
                            sRef.putFile(uri)
                                .addOnSuccessListener { taskSnapshot ->
                                    taskSnapshot.metadata!!.reference!!.downloadUrl
                                        .addOnSuccessListener { url ->
                                            // update image uri in firestore database
//                                            val userImageUpdate = hashMapOf(
//                                                "pasPhoto" to url,
//                                                "uid" to user!!.uid.toString()
//                                            )
//                                            db.collection("sim")
//                                                .document(user!!.uid)
//                                                .set(userImageUpdate, SetOptions.merge())
//                                                .addOnSuccessListener { documentReference ->
//                                                    Log.d(
//                                                        ContentValues.TAG,
//                                                        "DocumentSnapshot successfully written!"
//                                                    )
//                                                }
                                            Log.e("pasPhoto", url.toString())
                                            this.pasPhotoFilePath = url.toString()
                                        }.addOnFailureListener { exception ->
                                            Toast.makeText(
                                                this,
                                                exception.message,
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                }
//                            binding.tvPasPhotoPath.text = file.name

                        } else if (this.fileSelected == TTD_PHOTO) {
                            val imageExtension = MimeTypeMap.getSingleton()
                                .getExtensionFromMimeType(
                                    contentResolver.getType(
                                        uri
                                    )
                                )

                            // upload photo to google storage
                            val sRef: StorageReference =
                                FirebaseStorage.getInstance().reference.child(
                                    "ttdsim-" + user!!.uid.toString() + "-" + System.currentTimeMillis() + "." + imageExtension
                                )
                            sRef.putFile(uri)
                                .addOnSuccessListener { taskSnapshot ->
                                    taskSnapshot.metadata!!.reference!!.downloadUrl
                                        .addOnSuccessListener { url ->
                                            // update image uri in firestore database
//                                            val userImageUpdate = hashMapOf(
//                                                "ttd" to url,
//                                                "uid" to user!!.uid.toString()
//                                            )
//                                            db.collection("sim")
//                                                .document(user!!.uid)
//                                                .set(userImageUpdate, SetOptions.merge())
//                                                .addOnSuccessListener { documentReference ->
//                                                    Log.d(
//                                                        ContentValues.TAG,
//                                                        "DocumentSnapshot successfully written!"
//                                                    )
//                                                }
                                            Log.e("ttd", url.toString())
                                            this.ttdPhotoFilePath = url.toString()
                                        }.addOnFailureListener { exception ->
                                            Toast.makeText(
                                                this,
                                                exception.message,
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                }
//                            binding.tvSignaturePhotoPath.text = file.name
//                            this.ttdPhotoFilePath = path
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
        data.type = "application/pdf"
        val result = Intent.createChooser(data, "Choose a file")
        sActivityResultLauncher.launch(data)
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
        val TTD_PHOTO = "ttd_photo"
        val PAS_PHOTO = "pas_photo"
    }
}