package id.coolva.metapol.ui.main.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import id.coolva.metapol.R
import id.coolva.metapol.databinding.FragmentHomeBinding
import id.coolva.metapol.databinding.FragmentProfileBinding
import id.coolva.metapol.ui.auth.LoginActivity
import id.coolva.metapol.ui.main.MainActivity
import id.coolva.metapol.utils.Constants
import id.coolva.metapol.utils.Preferences


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var preferences: Preferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // setup preference
        preferences = Preferences(requireContext())

//        binding.btnLogout.setOnClickListener {
//            // reset data
//            preferences.setValues(Constants.USER_NAME, null)
//            preferences.setValues(Constants.USER_EMAIL, null)
//            preferences.setValues(Constants.USER_PHONE_NUMBER, null)
////                preferences.setValues(Constants.USER_PHOTO_PATH, user.profilePhoto)
//            preferences.setValues(Constants.USER_LOGIN_STATUS, "0") // if equal to 1, that means user is logged in
//
//            val moveToMain = Intent(requireContext(), LoginActivity::class.java)
//            startActivity(moveToMain)
//            activity?.finish()
//        }

        binding.btnLogout.setOnClickListener{
            FirebaseAuth.getInstance().signOut()

            startActivity(Intent(requireContext(), LoginActivity::class.java))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}