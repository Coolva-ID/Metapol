package id.coolva.metapol.ui.form.escortreq

import android.app.DatePickerDialog
import android.content.ContentValues
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.MaterialTimePicker.INPUT_MODE_KEYBOARD
import com.google.android.material.timepicker.TimeFormat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import id.coolva.metapol.R
import id.coolva.metapol.core.domain.model.EscortReq
import id.coolva.metapol.databinding.ActivityEscortRequestBinding
import id.coolva.metapol.core.data.testing.User
import java.util.*

@AndroidEntryPoint
class EscortRequestActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEscortRequestBinding
    private val viewModel: EscortReqViewModel by viewModels()
    val mAuth = FirebaseAuth.getInstance()
    val user: FirebaseUser? = mAuth.currentUser
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEscortRequestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = supportActionBar
        actionBar!!.title = "Permohonan Pengawalan"
        actionBar!!.setBackgroundDrawable(ColorDrawable(0xFFFFFFFF.toInt()))
        actionBar!!.elevation = 0F

        // set Dropdown for Jenis Kendaraan Pengawal
        val kendaraanPolisi: Array<String> = resources.getStringArray(R.array.kendaraan_polisi)
        val arrayAdapterKendaraanPolisi: ArrayAdapter<String> =
            ArrayAdapter(this, android.R.layout.simple_list_item_1, kendaraanPolisi)
        binding.actJenisKendaraan.setAdapter(arrayAdapterKendaraanPolisi)

        /**
         *  Setup Date Picker
         */
        // Build constraints.
        val constraintsBuilder =
            CalendarConstraints.Builder()
                .setStart(MaterialDatePicker.todayInUtcMilliseconds())
                .setValidator(DateValidatorPointForward.now()) // Makes only dates from today forward selectable.


        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Pilih Tanggal Keberangkatan")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .setInputMode(MaterialDatePicker.INPUT_MODE_TEXT)
                .setCalendarConstraints(constraintsBuilder.build())
                .build()

        /**
         *  Setup Time Picker
         */
        val timePicker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setHour(12)
            .setMinute(10)
            .setTitleText("Pilih Jam Keberangkatan")
            .setInputMode(INPUT_MODE_KEYBOARD)
            .build()


        binding.apply {
            edtTanggal.keyListener = null // edit text non editable
            edtJam.keyListener = null // edit text non editable

            tilTanggal.setOnClickListener {
                datePicker.show(supportFragmentManager, "DATE_PICKER")
            }

            datePicker.addOnPositiveButtonClickListener {
                edtTanggal.setText(datePicker.headerText)
            }

            edtJam.setOnClickListener {
                timePicker.show(supportFragmentManager, "TIME_PICKER")
            }

            timePicker.addOnPositiveButtonClickListener {
                edtJam.setText("${timePicker.hour}:${timePicker.minute}")
            }

            btnSubmit.setOnClickListener {
                submitData()
            }
        }
    }

    private fun submitData() {
        var isNotEmpty = true
        binding.apply {
            val escortPurpose = edtKeperluan.text.toString()
            val departureDate = edtTanggal.text.toString()
            val departureTime = edtJam.text.toString()
            val departurePoint = edtTitikKeberangkatan.text.toString()
            val destinationPoint = edtTitikAkhir.text.toString()
            val jumlahDikawal = edtJumlahKendaranYangDikawal.text.toString()
            val escortVehicleType = actJenisKendaraan.text.toString()
            val numOfEscortVehicle = edtJumlahKendaranPengawal.text.toString()

            if (escortPurpose.isEmpty()){
                isNotEmpty = false
                edtKeperluan.error = "Keperluan belum diisi"
            }
            if (departureDate.isEmpty()){
                isNotEmpty = false
                edtTanggal.error = "Anda belum memilih tanggal"
            }
            if (departureTime.isEmpty()){
                isNotEmpty = false
                edtJam.error = "Anda belum memilih waktu"
            }
            if (departurePoint.isEmpty()){
                isNotEmpty = false
                edtTitikKeberangkatan.error = "Titik Keberangkatan belum diisi"
            }
            if (destinationPoint.isEmpty()){
                isNotEmpty = false
                edtTitikAkhir.error = "Titik Tujuan belum diisi"
            }
            if (jumlahDikawal.isEmpty()){
                isNotEmpty = false
                edtJumlahKendaranYangDikawal.error = "Jumlah belum diisi"
            }
            if (escortVehicleType.isEmpty()){
                isNotEmpty = false
                actJenisKendaraan.error = "Anda belum memilih Jenis Kendaraan"
            }
            if (numOfEscortVehicle.isEmpty()){
                isNotEmpty = false
                edtJumlahKendaranPengawal.error = "Jumlah belum diisi"
            }

            if (isNotEmpty){
                val escortReq = hashMapOf(
                    "uid" to user!!.uid,
                    "escortPurpose" to escortPurpose,
                    "departureDate" to departureDate,
                    "departureTime" to departureTime,
                    "departurePoint" to departurePoint,
                    "destinationPoint" to destinationPoint,
                    "jumlahDikawal" to jumlahDikawal,
                    "escortVehicleType" to escortVehicleType,
                    "numOfEscortVehicle" to numOfEscortVehicle.toInt(),
                    "status" to "Menunggu Verifikasi",
                    "pengajuanAt" to System.currentTimeMillis().toString()
                )
                db.collection("escort")
                    .document(user!!.uid)
                    .set(escortReq, SetOptions.merge())
                    .addOnSuccessListener { documentReference ->
                        Log.d(
                            ContentValues.TAG,
                            "DocumentSnapshot successfully written!"
                        )
                    }
                val firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
                val userInDB: DocumentReference = db.collection("users").document(firebaseUser!!.uid)
                userInDB.get()
                    .addOnSuccessListener { document ->
                        val mUser = document.toObject(User::class.java)!!
                        val escortUpdate = hashMapOf(
                            "kawal" to mUser.kawal+1
                        )
                        db.collection("users")
                            .document(user!!.uid)
                            .set(escortUpdate, SetOptions.merge())
                    }
//                viewModel.insertEscortReq(escortReq)
                Toast.makeText(this@EscortRequestActivity, "Permohonan Pengawalan berhasil Diajukan", Toast.LENGTH_SHORT).show()
                onBackPressed()
                finish()
            }
        }
    }
}