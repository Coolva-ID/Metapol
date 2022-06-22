package id.coolva.metapol.ui.main.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import id.coolva.metapol.databinding.FragmentHistoryProcessBinding
import id.coolva.metapol.ui.form.escortreq.EscortReqAdapter
import id.coolva.metapol.ui.form.escortreq.EscortReqViewModel
import id.coolva.metapol.ui.form.simreg.SimRegViewModel
import id.coolva.metapol.ui.form.skckreg.SkckRegViewModel

@AndroidEntryPoint
class HistoryProcessFragment : Fragment() {

    private var _binding: FragmentHistoryProcessBinding? = null
    private val binding get() = _binding!!

    private val simRegViewModel: SimRegViewModel by viewModels()
    private val skckRegViewModel: SkckRegViewModel by viewModels()
    private val escortReqViewModel: EscortReqViewModel by viewModels()
    private lateinit var escortReqAdapter: EscortReqAdapter

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

        // setup Adapter and Recycler View for Escort Request Data
        escortReqAdapter = EscortReqAdapter()
        binding.rvEscortReq.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = escortReqAdapter
        }

        observeSimRegData()
        observeSkckRegData()
        observeEscortRequestData()
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

    private fun observeSkckRegData(){
        skckRegViewModel.getSKCKRegList().observe(viewLifecycleOwner){
            if (it.isNotEmpty() && it != null){
                val skckReg = it[0]
                if (skckReg != null){
                    binding.apply {
                        cardSkckReg.visibility = View.VISIBLE
                        tvSkckRegStats.text = "Status: " + skckReg.status
                    }
                }
            }
        }
    }

    private fun observeEscortRequestData() {
        escortReqViewModel.getEscortRequestList().observe(viewLifecycleOwner){ list ->
            if (list != null){
                escortReqAdapter.setData(list)
                escortReqAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}