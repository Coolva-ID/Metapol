package id.coolva.metapol.ui.auth

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import id.coolva.metapol.R
import id.coolva.metapol.core.domain.model.User
import id.coolva.metapol.databinding.ActivityRegisterBinding
import id.coolva.metapol.ui.main.MainActivity
import id.coolva.metapol.ui.main.profile.UserViewModel
import id.coolva.metapol.utils.Constants
import id.coolva.metapol.utils.DummyData
import id.coolva.metapol.utils.Preferences

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: UserViewModel by viewModels()
    private val userList = ArrayList<User>()
    private lateinit var preferences: Preferences

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

    private fun registerNewAccount() {
        var validInput = true
        binding.apply {
            val fullName = edtNameRegister.text.toString()
            val email = edtEmailRegister.text.toString()
            val password = edtPwRegister.text.toString()
            val confirmationPassword = edtContPwRegister.text.toString()

            if (fullName.isEmpty()){
                validInput = false
                edtNameRegister.error = "Nama belum diisi"
            } else if (fullName.length <= 5){
                validInput = false
                edtNameRegister.error = "Nama terlalu pendek"
            } else if (fullName.length > 21){
                validInput = false
                edtNameRegister.error = "Nama tidak bisa lebih dari 20 karakter"
            }

            if (email.isEmpty()){
                validInput = false
                edtEmailRegister.error = "Email belum diisi"
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                validInput = false
                edtEmailRegister.error = "Email tidak valid"
            } else if (emailAlreadyUsed(email)){
                validInput = false
                edtEmailRegister.error = "Email sudah digunakan silahkan login"
            }

            if (password.isEmpty()){
                validInput = false
                edtPwRegister.error = "Password belum diisi"
            } else if (password.length < 8){
                validInput = false
                edtPwRegister.error = "Password terlalu pendek"
            }

            if (confirmationPassword.isEmpty()){
                validInput = false
                edtContPwRegister.error = "Konfirmasi Password belum diisi"
            } else if (confirmationPassword != password){
                validInput = false
                edtContPwRegister.error = "Password tidak cocok"
            }

            if (validInput){
                val user = User(
                    name = fullName,
                    email = email,
                    password = password,
                    phoneNumber = null,
                    profilePhoto = null
                )
                viewModel.insertUser(user)

                // save to preference
                preferences.setValues(Constants.USER_NAME, user.name)
                preferences.setValues(Constants.USER_EMAIL, user.email)
                preferences.setValues(Constants.USER_PHONE_NUMBER, user.phoneNumber)
//                preferences.setValues(Constants.USER_PHOTO_PATH, user.profilePhoto)
                preferences.setValues(Constants.USER_LOGIN_STATUS, "1")

                val moveToMainActivity = Intent(this@RegisterActivity, MainActivity::class.java)
                startActivity(moveToMainActivity)
                finish()
            }
        }
    }

    private fun emailAlreadyUsed(email: String): Boolean {
        for (user in userList){
            if (user.email == email){
                return true
            }
        }
        return false
    }

    private var doubleBackToExitOnce:Boolean = false

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