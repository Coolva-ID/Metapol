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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import id.coolva.metapol.core.data.testing.User
import id.coolva.metapol.databinding.FragmentHomeBinding
import id.coolva.metapol.ui.form.simreg.SIMRegActivity
import id.coolva.metapol.ui.form.escortreq.EscortRequestActivity
import id.coolva.metapol.ui.form.simreg.SimRegViewModel

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val simRegViewModel: SimRegViewModel by viewModels()
    val firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    val db = Firebase.firestore

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
        db.collection("users").document(firebaseUser!!.uid)
            .get()
            .addOnSuccessListener { document ->
                user = document.toObject(User::class.java)!!
                Log.d("Name", user?.nama.toString())
                binding.tvUserName.text = user?.nama.toString()
            }

        binding.cardSim.setOnClickListener {
            simRegViewModel.getSIMRegistration().observe(viewLifecycleOwner){
                if (it.isNotEmpty() && it != null){
                    Toast.makeText(requireContext(), "Pendaftaran Ujian SIM anda dalam Proses.", Toast.LENGTH_SHORT).show()
                } else {
                    startActivity(Intent(requireContext(), SIMRegActivity::class.java))
                }
            }
        }

        binding.cardPengawalan.setOnClickListener {
            startActivity(Intent(requireContext(), EscortRequestActivity::class.java))
        }

        binding.btnClearData.setOnClickListener {
            simRegViewModel.getSIMRegistration().observe(viewLifecycleOwner){
                if (it.isNotEmpty() && it != null){
                    simRegViewModel.deleteSIMReg()
                    Toast.makeText(requireContext(), "Duh keapus cok", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "awww", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}