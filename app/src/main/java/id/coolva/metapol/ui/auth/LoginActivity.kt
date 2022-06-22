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
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.AndroidEntryPoint
import id.coolva.metapol.core.domain.model.User
import id.coolva.metapol.databinding.ActivityLoginBinding
import id.coolva.metapol.ui.main.MainActivity
import id.coolva.metapol.ui.main.profile.UserViewModel
import id.coolva.metapol.utils.Constants
import id.coolva.metapol.utils.Constants.Companion.USER_NAME
import id.coolva.metapol.utils.DummyData
import id.coolva.metapol.utils.Preferences

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: UserViewModel by viewModels()
    private val userList = ArrayList<User>()
    private lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // setup preference
        preferences = Preferences(this)
        
        // check if user already login, then intent to MainActivity
//        if (preferences.getValues("loginStatus").equals("1")){
//            finishAffinity()
//
//            val moveToMain = Intent(this@LoginActivity, MainActivity::class.java)
//            startActivity(moveToMain)
//        }
        
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
                                    Toast.makeText(
                                        this,
                                        "Berhasil masuk!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    val moveToMain = Intent(this, MainActivity::class.java)
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

    private fun observeUserList() {
        viewModel.getUserList().observe(this) { list ->
            if (list != null) {
                if (list.isEmpty()) {
                    val userList = DummyData.provideUserList()
                    viewModel.insertUser(userList[0])
                    viewModel.insertUser(userList[1])
                } else if (list.isNotEmpty()) {
                    this.userList.addAll(list)
                    Log.e("LoginActivity: ", list.toString())
                    Log.e("LoginActivity: ", this.userList.toString())
                }
            }
        }
    }

    private fun login() {
        var inputValid = true

        binding.apply {
            // get user input
            val userEmail = edtEmailLogin.text.toString()
            val userPassword = edtPwLogin.text.toString()

            if (userEmail.isEmpty()) {
                edtEmailLogin.error = "Anda belum memasukkan email"
                inputValid = false
            }

            if (userPassword.isEmpty()) {
                edtPwLogin.error = "Anda belum memasukkan password"
                inputValid = false
            }

            if (inputValid) {
                loginVerification(userEmail, userPassword)
            }
        }
    }

    private fun loginVerification(inputEmail: String, inputPassword: String) {

        val user: User? = getUser(inputEmail)
        Log.e("LoginActivity: ", user.toString())
        Log.e("LoginActivity: ", user?.password.toString())
        Log.e("LoginActivity: ", inputPassword)
        Log.e("LoginActivity: ", (user?.password == inputPassword).toString())

        if (user != null){
            if (user.password == inputPassword){
//                // if input valid, save data and intent to Main Activity
//                preferences.setValues(USER_NAME, user.name)
//                preferences.setValues(Constants.USER_EMAIL, user.email)
//                preferences.setValues(Constants.USER_PHONE_NUMBER, user.phoneNumber)
////                preferences.setValues(Constants.USER_PHOTO_PATH, user.profilePhoto)
//                preferences.setValues(Constants.USER_LOGIN_STATUS, "1") // if equal to 1, that means user is logged in
//
//                val moveToMain = Intent(this@LoginActivity, MainActivity::class.java)
//                startActivity(moveToMain)
//                finish()
            } else {
                binding.edtPwLogin.error = "Password salah."
            }
        } else {
            binding.edtEmailLogin.error = "Email tidak terdaftar."
        }
    }

    private fun getUser(inputEmail: String): User?{
        for (user in userList) {
            if (user.email == inputEmail) {
                return user
            }
        }
        return null
    }


    private var doubleBackToExitOnce: Boolean = false

    override fun onBackPressed() {
        if (doubleBackToExitOnce) {
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
