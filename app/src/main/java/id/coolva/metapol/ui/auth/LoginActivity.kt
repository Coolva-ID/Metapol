package id.coolva.metapol.ui.auth

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import id.coolva.metapol.core.data.testing.User
import id.coolva.metapol.databinding.ActivityLoginBinding
import id.coolva.metapol.ui.main.MainActivity
import id.coolva.metapol.ui.main.profile.UserViewModel
import id.coolva.metapol.utils.Constants
import id.coolva.metapol.utils.Constants.Companion.USER_LOGIN_STATUS
import id.coolva.metapol.utils.DummyData
import id.coolva.metapol.utils.Preferences

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: UserViewModel by viewModels()
//    private val userList = ArrayList<User>()
    private lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // setup preference
        preferences = Preferences(this)

        // check if user already login, then intent to MainActivity
        if (preferences.getValues(Constants.USER_LOGIN_STATUS).equals("1")){
            finishAffinity()

            val moveToMain = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(moveToMain)
        }
        
//        observeUserList()

//        binding.btnLogin.setOnClickListener {
//            login()
//        }

        binding.btnLogin.setOnClickListener {
            when {
                TextUtils.isEmpty(binding.edtEmailLogin.text.toString().trim() {it <= ' '}) -> {
                    binding.edtEmailLogin.error = "Anda belum memasukkan Email"
                }
                TextUtils.isEmpty(binding.edtPwLogin.text.toString().trim() {it <= ' '}) -> {
                    binding.edtPwLogin.error = "Anda belum memasukkan Password"
                }
                else -> {
                    val email:String = binding.edtEmailLogin.text.toString().trim() {it <= ' '}
                    val password:String = binding.edtPwLogin.text.toString()

                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(
                            OnCompleteListener<AuthResult> { task ->
                                if (task.isSuccessful) {
                                    val lastLogin = hashMapOf(
                                        "lastLoginAt" to System.currentTimeMillis().toString()
                                    )
                                    val db = Firebase.firestore
                                    db.collection("users")
                                        .document(FirebaseAuth.getInstance().uid.toString())
                                        .set(lastLogin, SetOptions.merge())

                                    Toast.makeText(
                                        this,
                                        "Berhasil masuk!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    val moveToMain = Intent(this, MainActivity::class.java)

                                    preferences.setValues(USER_LOGIN_STATUS, "1")
                                    startActivity(moveToMain)
                                    finish()
                                } else {
                                    Toast.makeText(
                                        this,
                                        task.exception!!.message.toString(),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        )
                }
            }
        }

        binding.btnRegisterInLogin.setOnClickListener {
            val moveToLogin = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(moveToLogin)
            finish()
        }
    }

    private var doubleBackToExitOnce: Boolean = false

    override fun onBackPressed() {
        if (doubleBackToExitOnce) {
            val builder = MaterialAlertDialogBuilder(this)
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
