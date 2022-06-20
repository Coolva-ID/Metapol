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


class DaftarSIM : AppCompatActivity() {

    private val binding by lazy {
        ActivityDaftarSimBinding.inflate(layoutInflater)
    }
    private var fileUri = ""
    private var filepath = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        val actionBar = supportActionBar
        actionBar!!.title = "Pendaftaran Ujian SIM"
        actionBar!!.setBackgroundDrawable(ColorDrawable(0xFFFFFFFF.toInt()))
        actionBar!!.elevation = 0F

        val golSim: Array<String> = resources.getStringArray(R.array.golongan_sim)
        val arrayAdapterGolSim: ArrayAdapter<String> =
            ArrayAdapter(this, android.R.layout.simple_list_item_1, golSim)
        binding.actGolSim.setAdapter(arrayAdapterGolSim)

        val sertSim: Array<String> = resources.getStringArray(R.array.sert_mengemudi)
        val arrayAdapterSertSim: ArrayAdapter<String> =
            ArrayAdapter(this, android.R.layout.simple_list_item_1, sertSim)
        binding.actSertMengemudi.setAdapter(arrayAdapterSertSim)

        binding.btnUploadSignature.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                // when permission is not granted
                // request permission
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE) ,
                    1
                )
            } else {
                // when permission is granted
                selectPDF()
            }
        }
    }


    var sActivityResultLauncher = registerForActivityResult(
        StartActivityForResult(),
        ActivityResultCallback<ActivityResult> { result ->
            if (result.resultCode == RESULT_OK) {
                val data: Intent? = result.data
                if (data != null){
                    // get PDF uri
                    val uri: Uri? = data.data
                    Log.e("DAFTAR SIM URI: ", uri.toString())

                    // get PDF path
                    val path = uri?.path
                    Log.e("DAFTAR SIM PATH: ", path.toString())
                }
            }
        }
    )

    private fun selectPDF() {
            val data = Intent(Intent.ACTION_OPEN_DOCUMENT)
            data.type = "application/pdf"
            val result = Intent.createChooser(data, "Choose a file")
            sActivityResultLauncher.launch(data)
            Log.e("DAFTAR SIM RESULT: ", result.toString())
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // check condition
        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            selectPDF()
        } else {
            Toast.makeText(this, "Permission Denied.", Toast.LENGTH_SHORT).show()
        }
    }
}