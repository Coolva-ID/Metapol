package id.coolva.metapol.ui.main.form

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import id.coolva.metapol.R
import id.coolva.metapol.databinding.ActivityDaftarSimBinding
import java.io.File


class DaftarSIM : AppCompatActivity() {

    private val binding by lazy {
        ActivityDaftarSimBinding.inflate(layoutInflater)
    }

    private var fileSelected = ""
    private var ttdPhotoFilePath: String = ""
    private var pasPhotoFilePath: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        val actionBar = supportActionBar
        actionBar!!.title = "Pendaftaran Ujian SIM"
        actionBar!!.setBackgroundDrawable(ColorDrawable(0xFFFFFFFF.toInt()))
        actionBar!!.elevation = 0F

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
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // when permission is not granted
                // request permission
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    1
                )
            } else {
                // when permission is granted
                selectPDF()
                this.fileSelected = TTD_PHOTO
            }
        }

        binding.btnUploadPasPhoto.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // when permission is not granted
                // request permission
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    1
                )
            } else {
                // when permission is granted
                selectPDF()
                this.fileSelected = PAS_PHOTO
            }
        }

        binding.btnSubmit.setOnClickListener {
            var isNotEmpty = true



            binding.apply {
                // get data from user input
                val golonganSimValue = actGolSim.text.toString()
                val sertMengemudiValue = actSertMengemudi.text.toString()
                val ttdPhotoValue = ttdPhotoFilePath
                val pasPhotoValue = pasPhotoFilePath
                val contactNameValue = edtContactName.text.toString()
                val contactAddressValue = edtContactAddress.text.toString()
                val contactPhoneValue = edtContactPhone.text.toString()

                Log.e("Check form SIM:", golonganSimValue)
                Log.e("Check form SIM:", sertMengemudiValue)
                Log.e("Check form SIM:", ttdPhotoValue)
                Log.e("Check form SIM:", pasPhotoValue)
                Log.e("Check form SIM:", contactNameValue)
                Log.e("Check form SIM:", contactAddressValue)
                Log.e("Check form SIM:", contactPhoneValue)

                if (golonganSimValue.isEmpty()){
                    actGolSim.error = "Anda belum memilih Golongan SIM"
                    isNotEmpty = false
                }
                if (sertMengemudiValue.isEmpty()){
                    actSertMengemudi.error = "Anda belum mengisi Sertifikat Mengemudi"
                    isNotEmpty = false
                }

                if (ttdPhotoValue.isEmpty()){
                    Toast.makeText(this@DaftarSIM, "Anda belum menambahkan Photo TTD", Toast.LENGTH_SHORT).show()
                    isNotEmpty = false
                }

                if (pasPhotoValue.isEmpty()){
                    Toast.makeText(this@DaftarSIM, "Anda belum menambahkan Pas Photo", Toast.LENGTH_SHORT).show()
                    isNotEmpty = false
                }

                if (contactNameValue.isEmpty()){
                    edtContactName.error = "Nama belum diisi"
                    isNotEmpty = false
                }
                if (contactAddressValue.isEmpty()){
                    edtContactAddress.error = "Alamat belum diisi"
                    isNotEmpty = false
                }
                if (contactPhoneValue.isEmpty()){
                    edtContactPhone.error = "No. Telepon belum diisi"
                    isNotEmpty = false
                }
            }

            if (isNotEmpty){
                // TODO: create new UjianSIMEntity and direct to history screen
            }
        }
    }


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
                    if (path != null){
                        val file = File(path)
                        Log.e("DAFTAR SIM NAMA FILE: ", file.name)


                        if (this.fileSelected == PAS_PHOTO){
                            binding.tvPasPhotoPath.text = file.name
                            this.pasPhotoFilePath = path

                        } else if (this.fileSelected == TTD_PHOTO){
                            binding.tvSignaturePhotoPath.text = file.name
                            this.ttdPhotoFilePath = path
                        }
                    } else {
                        Toast.makeText(this, "File belum dipilih", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    )

    private fun selectPDF() {
        val data = Intent(Intent.ACTION_OPEN_DOCUMENT)
        data.type = "application/pdf"
        val result = Intent.createChooser(data, "Choose a file")
        sActivityResultLauncher.launch(data)
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