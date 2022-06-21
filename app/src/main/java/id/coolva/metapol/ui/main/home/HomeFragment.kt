package id.coolva.metapol.ui.main.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import id.coolva.metapol.databinding.FragmentHomeBinding
import id.coolva.metapol.ui.form.simreg.SIMRegActivity
import id.coolva.metapol.ui.form.escortreq.EscortRequestActivity


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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
        binding.cardSim.setOnClickListener {
            startActivity(Intent(requireContext(), SIMRegActivity::class.java))
        }

        binding.cardPengawalan.setOnClickListener {
            startActivity(Intent(requireContext(), EscortRequestActivity::class.java))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}