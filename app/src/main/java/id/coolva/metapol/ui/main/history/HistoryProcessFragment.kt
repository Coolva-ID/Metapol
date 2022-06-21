package id.coolva.metapol.ui.main.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import id.coolva.metapol.R
import id.coolva.metapol.databinding.FragmentHistoryProcessBinding
import id.coolva.metapol.ui.form.simreg.SimRegViewModel

@AndroidEntryPoint
class HistoryProcessFragment : Fragment() {

    private var _binding: FragmentHistoryProcessBinding? = null
    private val binding get() = _binding!!

    private val simRegViewModel: SimRegViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHistoryProcessBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeSimRegData()
    }

    private fun observeSimRegData() {
        simRegViewModel.getSIMRegistration().observe(viewLifecycleOwner){
            if (it.isNotEmpty()  && it != null){
                val simReg = it[0]
                if (simReg != null){
                    binding.apply {
                        cardSimReg.visibility = View.VISIBLE
                        tvSimRegStats.text = "Status: " + simReg.status
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}