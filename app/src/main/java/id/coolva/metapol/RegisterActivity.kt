package id.coolva.metapol

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AlertDialog
import id.coolva.metapol.databinding.ActivityLoginBinding
import id.coolva.metapol.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnRegister.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0: View?) {
                val moveToMainActivity = Intent(this@RegisterActivity, MainActivity::class.java)
                startActivity(moveToMainActivity)
                finish()
            }
        })

        binding.btnLoginInRegister.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0: View?) {
                val moveToLoginActivity = Intent(this@RegisterActivity, LoginActivity::class.java)
                startActivity(moveToLoginActivity)
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

            val alertDialog: AlertDialog = builder.create()
            alertDialog.show()
        }

        this.doubleBackToExitOnce = true

        Handler().postDelayed({
            kotlin.run { doubleBackToExitOnce = false }
        }, 2000)
    }
}