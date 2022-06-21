package id.coolva.metapol.ui.auth

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AlertDialog
import id.coolva.metapol.databinding.ActivityLoginBinding
import id.coolva.metapol.ui.main.MainActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnLogin.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0: View?) {
                val moveToMain = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(moveToMain)
                finish()
            }
        })

        binding.btnRegisterInLogin.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0: View?) {
                val moveToLogin = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(moveToLogin)
                finish()
            }
        })
    }

    var doubleBackToExitOnce:Boolean = false

    override fun onBackPressed() {
        if(doubleBackToExitOnce){
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Keluar Aplikasi")
            builder.setMessage("Apakah yakin ingin keluar?")
                .setCancelable(false)
                .setPositiveButton("Yakin", DialogInterface.OnClickListener { dialogInterface, i ->
                    finish()
                })
                .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialogInterface, i ->
                    dialogInterface.cancel()
                })

            val alertDialog:AlertDialog = builder.create()
            alertDialog.show()
        }

        this.doubleBackToExitOnce = true

        Handler().postDelayed({
            kotlin.run { doubleBackToExitOnce = false }
        }, 2000)
    }
}
