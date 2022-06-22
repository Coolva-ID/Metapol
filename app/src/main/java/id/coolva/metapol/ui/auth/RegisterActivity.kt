package id.coolva.metapol.ui.auth

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import id.coolva.metapol.R
import id.coolva.metapol.databinding.ActivityRegisterBinding
import id.coolva.metapol.ui.main.MainActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

//        binding.btnRegister.setOnClickListener(object: View.OnClickListener {
//            override fun onClick(p0: View?) {
//                val moveToMainActivity = Intent(this@RegisterActivity, MainActivity::class.java)
//                startActivity(moveToMainActivity)
//                finish()
//            }
//        })

        binding.btnLoginInRegister.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0: View?) {
                val moveToLoginActivity = Intent(this@RegisterActivity, LoginActivity::class.java)
                startActivity(moveToLoginActivity)
                finish()
            }
        })

        binding.btnRegister.setOnClickListener{
            when {
                TextUtils.isEmpty(binding.edtNameRegister.text.toString()) -> {
                    binding.edtNameRegister.error = "Anda belum memasukkan Nama"
                }
                TextUtils.isEmpty(binding.edtEmailRegister.text.toString().trim() {it <= ' '}) -> {
                    binding.edtEmailRegister.error = "Anda belum memasukkan Email"
                }
                TextUtils.isEmpty(binding.edtPwRegister.text.toString()) -> {
                    binding.edtPwRegister.error = "Anda belum memasukkan Password"
                }
                TextUtils.isEmpty(binding.edtContPwRegister.text.toString()) -> {
                    binding.edtContPwRegister.error = "Anda belum memasukkan Konfirmasi Password"
                }
                binding.edtPwRegister.text.toString() != binding.edtContPwRegister.text.toString() -> {
                    binding.edtContPwRegister.error = "Password Anda berbeda"
                }
                else -> {
                    val email:String = binding.edtEmailRegister.text.toString().trim{ it <= ' '}
                    val password:String = binding.edtPwRegister.text.toString()
                    val conPassword:String = binding.edtContPwRegister.text.toString()
                    val fullName:String = binding.edtNameRegister.text.toString()

                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, conPassword)
                        .addOnCompleteListener(
                            OnCompleteListener<AuthResult> { task ->
                                if (task.isSuccessful) {
                                    val firebaseUser: FirebaseUser = task.result!!.user!!

                                    Toast.makeText(
                                        this,
                                        "Anda berhasil terdaftar",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    firebaseUser!!.sendEmailVerification()

                                    val db = Firebase.firestore

                                    val newUser = hashMapOf(
                                        "nama" to fullName,
                                        "email" to email
                                    )

                                    db.collection("users")
                                        .document(firebaseUser.uid!!)
                                        .set(newUser)
                                        .addOnSuccessListener {
                                            val moveToMainActivity = Intent(this@RegisterActivity, MainActivity::class.java)
                                            startActivity(moveToMainActivity)
                                            finish()
                                        }

                                }
                            }
                        )
                }
            }
        }
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