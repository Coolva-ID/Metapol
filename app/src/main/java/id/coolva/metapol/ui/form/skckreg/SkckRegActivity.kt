package id.coolva.metapol.ui.form.skckreg

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val actionBar = supportActionBar
        actionBar!!.title = "Pendaftaran SKCK"
        actionBar!!.setBackgroundDrawable(ColorDrawable(0xFFFFFFFF.toInt()))
        actionBar!!.elevation = 0F

        binding.btnUploadKtp.setOnClickListener {
            if (allowedReadExternalStorage(this)) {
                selectPDF()
                this.selectedInput = KTP
            } else {
                requestReadExternalStorage(this)
            }
        }

        binding.btnUploadAkte.setOnClickListener {
            if (allowedReadExternalStorage(this)) {
                selectPDF()
                this.selectedInput = AKTE_LAHIR
            } else {
                requestReadExternalStorage(this)
            }
        }

        binding.btnUploadKk.setOnClickListener {
            if (allowedReadExternalStorage(this)) {
                selectPDF()
                this.selectedInput = KARTU_KELUARGA
            } else {
                requestReadExternalStorage(this)
            }
        }

        binding.btnUploadPasPhoto.setOnClickListener {
            if (allowedReadExternalStorage(this)) {
                selectPDF()
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
                                binding.tvKtpPath.text = file.name
                                this.ktpPath = path

                            }
                            AKTE_LAHIR -> {
                                binding.tvAktePath.text = file.name
                                this.akteLahirPath = path
                            }
                            KARTU_KELUARGA -> {
                                binding.tvKtpPath.text = file.name
                                this.kartuKeluargaPath = path
                            }
                            PAS_PHOTO -> {
                                binding.tvPasPhotoPath.text = file.name
                                this.pasPhotoPath = path
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

    /**
     * SUBMIT DATA
     */
    private fun submitData() {
        var inputValid = true
        binding.apply {
            if (ktpPath.isEmpty() && akteLahirPath.isEmpty() && kartuKeluargaPath.isEmpty() && pasPhotoPath.isEmpty()) {
                inputValid = false
            }

            if (inputValid) {
                val skckReg = SKCKReg(
                    identityCardPhotoPath = ktpPath,
                    aktePhotoPath = akteLahirPath,
                    kkPhotoPath = kartuKeluargaPath,
                    pasPhotoPath = pasPhotoPath,
                    status = "Menunggu Verifikasi"
                )
                viewModel.insertSKCKReg(skckReg)
                Toast.makeText(this@SkckRegActivity, "Pendaftaran berhasil dikirim", Toast.LENGTH_SHORT).show()
                onBackPressed()
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