package id.coolva.metapol.ui.form.escortreq

import android.app.DatePickerDialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
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
import dagger.hilt.android.AndroidEntryPoint
import id.coolva.metapol.R
import id.coolva.metapol.core.domain.model.EscortReq
import id.coolva.metapol.databinding.ActivityEscortRequestBinding
import java.util.*

@AndroidEntryPoint
class EscortRequestActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEscortRequestBinding
    private val viewModel: EscortReqViewModel by viewModels()

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
                val escortReq = EscortReq(
                    escortPurpose = escortPurpose,
                    departureDate = departureDate,
                    departureTime = departureTime,
                    departurePoint = departurePoint,
                    destinationPoint = destinationPoint,
                    jumlahDikawal = jumlahDikawal,
                    escortVehicleType = escortVehicleType,
                    numOfEscortVehicle = numOfEscortVehicle.toInt(),
                    status = "Menunggu Verifikasi"
                )
                viewModel.insertEscortReq(escortReq)
                Toast.makeText(this@EscortRequestActivity, "Permohonan Pengawalan berhasil Diajukan", Toast.LENGTH_SHORT).show()
                onBackPressed()
            }
        }
    }
}