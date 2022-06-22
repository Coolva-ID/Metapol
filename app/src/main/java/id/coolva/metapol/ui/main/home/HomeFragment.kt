package id.coolva.metapol.ui.main.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import id.coolva.metapol.databinding.FragmentHomeBinding
import id.coolva.metapol.ui.form.simreg.SIMRegActivity
import id.coolva.metapol.ui.form.escortreq.EscortRequestActivity
import id.coolva.metapol.ui.form.simreg.SimRegViewModel
import id.coolva.metapol.ui.form.skckreg.SkckRegActivity
import id.coolva.metapol.ui.form.skckreg.SkckRegViewModel

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val simRegViewModel: SimRegViewModel by viewModels()
    private val skckRegViewModel: SkckRegViewModel by viewModels()

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
            simRegViewModel.getSIMRegistration().observe(viewLifecycleOwner){
                if (it.isNotEmpty() && it != null){
                    Toast.makeText(requireContext(), "Pendaftaran Ujian SIM anda dalam proses.", Toast.LENGTH_SHORT).show()
                } else {
                    startActivity(Intent(requireContext(), SIMRegActivity::class.java))
                }
            }
        }

        binding.cardSkck.setOnClickListener {
            skckRegViewModel.getSKCKRegList().observe(viewLifecycleOwner){
                if (it.isNotEmpty() && it != null){
                    Toast.makeText(requireContext(), "Pendaftaran SKCK anda dalam proses.", Toast.LENGTH_SHORT).show()
                } else {
            startActivity(Intent(requireContext(), SkckRegActivity::class.java))
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