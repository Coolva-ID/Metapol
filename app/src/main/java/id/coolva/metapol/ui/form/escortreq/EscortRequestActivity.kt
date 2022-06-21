package id.coolva.metapol.ui.form.escortreq

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import id.coolva.metapol.databinding.ActivityEscortRequestBinding

class EscortRequestActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEscortRequestBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEscortRequestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val actionBar = supportActionBar
        actionBar!!.title = "Permohonan Pengawalan"
        actionBar!!.setBackgroundDrawable(ColorDrawable(0xFFFFFFFF.toInt()))
        actionBar!!.elevation = 0F


    }
}