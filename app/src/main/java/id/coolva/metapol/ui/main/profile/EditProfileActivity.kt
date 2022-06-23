package id.coolva.metapol.ui.main.profile

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import id.coolva.metapol.R
import id.coolva.metapol.databinding.ActivityEditProfileBinding
import id.coolva.metapol.databinding.ActivitySkckRegBinding
import id.coolva.metapol.ui.main.MainActivity
import id.coolva.metapol.utils.Constants
import id.coolva.metapol.utils.Preferences

class EditProfileActivity : AppCompatActivity() {

    private val binding: ActivityEditProfileBinding by lazy {
        ActivityEditProfileBinding.inflate(layoutInflater)
    }

    private lateinit var preferences: Preferences
    private val mAuth = FirebaseAuth.getInstance()
    val user: FirebaseUser? = mAuth.currentUser
    private val db = Firebase.firestore

    private var fileSelected: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // setup action bar
        val actionBar = supportActionBar
        actionBar!!.title = "Edit Profile"
        actionBar!!.setBackgroundDrawable(ColorDrawable(0xFFFFFFFF.toInt()))
        actionBar!!.elevation = 0F

        // setup preference
        preferences = Preferences(this)

        loadDataFromPreferences()

        binding.apply {
            btnUpdate.setOnClickListener {
                when {
                    TextUtils.isEmpty(edtUserFullName.text.toString()) -> {
                        edtUserFullName.error = "Anda belum memasukkan Nama"
                    }
                    TextUtils.isEmpty(edtUserEmail.text.toString().trim() { it <= ' ' }) -> {
                        edtUserEmail.error = "Anda belum memasukkan Email"
                    }
                    TextUtils.isEmpty(edtUserPhoneNo.text.toString()) -> {
                        edtUserPhoneNo.error = "Anda belum memasukkan Nomor Telepon"
                    }

                    else -> {
                        val fullName: String = edtUserFullName.text.toString()
                        val email: String = edtUserEmail.text.toString().trim { it <= ' ' }
                        val phoneNumber: String = edtUserPhoneNo.text.toString().trim { it <= ' ' }

                        // TODO: Update data in firebase

                        // TODO: Update data in Local Preferences
//                        preferences.setValues(Constants.USER_NAME, fullName)
//                        preferences.setValues(Constants.USER_EMAIL, email)
//                        preferences.setValues(Constants.USER_PHONE_NUMBER, phoneNumber)
////                        preferences.setValues(Constants.USER_PHOTO_PATH, profilePhoto)

                        // TODO: Refresh Data
                    }
                }
            }
        }
    }

    private fun loadDataFromPreferences() {
        binding.apply {
            val userName = preferences.getValues(Constants.USER_NAME) ?: ""
            val userEmail = preferences.getValues(Constants.USER_EMAIL) ?: ""
            val userProfilePhoto = preferences.getValues(Constants.USER_PHOTO_PATH) ?: ""

            edtUserFullName.setText(userName)
            edtUserEmail.setText(userEmail)

            if (userProfilePhoto != "") {
                Glide.with(this@EditProfileActivity)
                    .load(userProfilePhoto)
                    .into(ivProfileImage)
            }
        }
    }
}