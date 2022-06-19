package id.coolva.metapol

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import id.coolva.metapol.databinding.ActivityLoginBinding
import id.coolva.metapol.ui.main.MainActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val moveToMain = Intent(this, MainActivity::class.java)
            startActivity(moveToMain)
        }
    }
}