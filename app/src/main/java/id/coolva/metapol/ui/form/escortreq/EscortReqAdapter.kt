package id.coolva.metapol.ui.form.escortreq

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.coolva.metapol.core.domain.model.EscortReq
import id.coolva.metapol.databinding.ItemHistoryProcessBinding

class EscortReqAdapter: RecyclerView.Adapter<EscortReqAdapter.ViewHolder>() {

    private var escortReqList = ArrayList<EscortReq>()

    class ViewHolder(private val binding: ItemHistoryProcessBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(escortReq: EscortReq){
            binding.apply {
                tvServiceTitle.text = "Pengajuan Pengawalan"
                tvServiceStatus.text = "Status: " + escortReq.status

            }
            itemView.setOnClickListener {
//                val moveToDetail = Intent(itemView.context, DetailActivity::class.java)
//                moveToDetail.putExtra(DetailActivity.EXTRA_ENTITY, escortReq)
//                itemView.context.startActivity(moveToDetail)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = ItemHistoryProcessBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val resultItem = escortReqList[position]
        holder.bind(resultItem)
    }

    override fun getItemCount(): Int = escortReqList.size

    fun setData(itemList: List<EscortReq>){
        this.escortReqList.clear()
        this.escortReqList.addAll(itemList)
    }
}