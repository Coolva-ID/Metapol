package id.coolva.metapol

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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