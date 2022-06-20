package id.coolva.metapol.ui.main.form

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.navigation.ui.AppBarConfiguration
import com.google.android.material.appbar.AppBarLayout
import id.coolva.metapol.R
import id.coolva.metapol.databinding.ActivityDaftarSimBinding

class DaftarSIM : AppCompatActivity() {
    private lateinit var binding: ActivityDaftarSimBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDaftarSimBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val actionBar = supportActionBar
        actionBar!!.title = "Pendaftaran Ujian SIM"
        actionBar!!.setBackgroundDrawable(ColorDrawable(0xFFFFFFFF.toInt()))

        val golSim:Array<String> = resources.getStringArray(R.array.golongan_sim)
        val arrayAdapterGolSim:ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_list_item_1, golSim)
        binding.actGolSim.setAdapter(arrayAdapterGolSim)

        val sertSim:Array<String> = resources.getStringArray(R.array.sert_mengemudi)
        val arrayAdapterSertSim:ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_list_item_1, sertSim)
        binding.actSertMengemudi.setAdapter(arrayAdapterSertSim)
    }
}