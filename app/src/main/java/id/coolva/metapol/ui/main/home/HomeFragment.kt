package id.coolva.metapol.ui.main.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import id.coolva.metapol.R
import id.coolva.metapol.core.data.testing.User
import id.coolva.metapol.databinding.FragmentHomeBinding
import id.coolva.metapol.ui.form.simreg.SIMRegActivity
import id.coolva.metapol.ui.form.escortreq.EscortRequestActivity
import id.coolva.metapol.ui.form.simreg.SimRegViewModel
import id.coolva.metapol.ui.form.skckreg.SkckRegActivity
import id.coolva.metapol.ui.form.skckreg.SkckRegViewModel
import id.coolva.metapol.ui.main.MainActivity
import id.coolva.metapol.utils.Constants
import id.coolva.metapol.utils.Preferences

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val simRegViewModel: SimRegViewModel by viewModels()
    val firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    val db = Firebase.firestore
    val userInDB: DocumentReference = db.collection("users").document(firebaseUser!!.uid)
    private val skckRegViewModel: SkckRegViewModel by viewModels()

    private lateinit var preferences: Preferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
        // Inflate the layout for this fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var user: User? = null

        //get user data from database
        userInDB.get()
            .addOnSuccessListener { document ->
                user = document.toObject(User::class.java)!!
                Log.d("Name", user?.nama.toString())
                Log.d("Photo", user?.foto_profil.toString())
                Log.d("SIM", user?.sim.toString())
                Log.d("SKCK", user?.skck.toString())
                Log.d("Photo", user?.kawal.toString())
                Log.d("Verified", user?.verified.toString())
                binding.tvUserName.text = user?.nama.toString()
                // get profile photo uri from database
                if (user!!.foto_profil.toString() != "") {
                    Glide.with(requireContext())
                        .load(user!!.foto_profil.toString())
                        .into(binding.ivUserProfileImage)
                } else {
                    Glide.with(requireContext())
                        .load(R.drawable.ic_default_profile)
                        .into(binding.ivUserProfileImage)
                }
                if (user!!.verified) {
                    binding.ivVerifiedLogo.visibility = View.VISIBLE
                }
            }.addOnFailureListener {
                Log.d("Data collect", "fail")
            }

        // setup preference
//        preferences = Preferences(requireContext())
//        val name = preferences.getValues(Constants.USER_NAME) ?: ""
//
//        if (name.isEmpty() || name == ""){
//            loadFromFirebase()
//        } else {
//            loadFromPreferences()
//        }
//        binding.apply {
//            val name = preferences.getValues(Constants.USER_NAME) ?: "User"
//            Log.e("HomeFragment: ", name.toString())
//            tvUserName.text = name
//        }

        binding.cardSim.setOnClickListener {
            if (user!!.sim != 0) {
                Toast.makeText(
                    requireContext(),
                    "Pendaftaran Ujian SIM anda dalam proses.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                startActivity(Intent(requireContext(), SIMRegActivity::class.java))
            }
        }

        binding.cardSkck.setOnClickListener {
            if (user!!.skck != 0) {
                Toast.makeText(
                    requireContext(),
                    "Pendaftaran SKCK anda dalam proses.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                startActivity(Intent(requireContext(), SkckRegActivity::class.java))
            }
        }

        binding.cardPengawalan.setOnClickListener {
            startActivity(Intent(requireContext(), EscortRequestActivity::class.java))
        }

        binding.btnClearData.setOnClickListener {
            simRegViewModel.getSIMRegistration().observe(viewLifecycleOwner) {
                if (it.isNotEmpty() && it != null) {
                    simRegViewModel.deleteSIMReg()
                    Toast.makeText(requireContext(), "Duh keapus cok", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "awww", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun loadFromFirebase() {
        var user: User? = null
        db.collection("users").document(firebaseUser!!.uid)
            .get()
            .addOnSuccessListener { document ->
                user = document.toObject(User::class.java)!!
                Log.e("HomeFragment:", user?.nama.toString())
                Log.e("HomeFragment:", user?.email.toString())
                Log.e("HomeFragment:", user?.foto_profil.toString())
                if (user != null){
                    preferences.setValues(Constants.USER_NAME, user?.nama)
                    preferences.setValues(Constants.USER_EMAIL, user?.email)
                    preferences.setValues(Constants.USER_PHOTO_PATH, user?.foto_profil)
                    preferences.setValues(
                        Constants.USER_LOGIN_STATUS,
                        "1"
                    ) // if equal to 1, that means user is logged in

                    // then display the data
                    loadFromPreferences()
                }
            }
    }

    private fun loadFromPreferences() {
        binding.apply {
            val name = preferences.getValues(Constants.USER_NAME) ?: "User"
            val profilePath = preferences.getValues(Constants.USER_PHOTO_PATH) ?: ""
            Log.e("HomeFragment: ", name.toString())
            Log.e("HomeFragment: ", profilePath.toString())
            Log.e("HomeFragment: ", preferences.getValues(Constants.USER_LOGIN_STATUS).toString())
            tvUserName.text = name
            if (profilePath != "") {
                Glide.with(requireContext())
                    .load(profilePath)
                    .into(binding.ivUserProfileImage)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}