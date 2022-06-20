package id.coolva.metapol.ui.main.form

import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import id.coolva.metapol.R
import id.coolva.metapol.databinding.ActivityPengawalanBinding

class Pengawalan : AppCompatActivity() {
    private lateinit var binding: ActivityPengawalanBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPengawalanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val actionBar = supportActionBar
        actionBar!!.title = "Permohonan Pengawalan"
        actionBar!!.setBackgroundDrawable(ColorDrawable(0xFFFFFFFF.toInt()))
        actionBar!!.elevation = 0F


    }
}