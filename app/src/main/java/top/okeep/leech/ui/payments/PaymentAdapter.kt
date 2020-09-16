package top.okeep.leech.ui.payments

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import top.okeep.leech.databinding.PaymentListItemBinding
import top.okeep.leech.models.Payment

class PaymentAdapter(private val items: List<Payment>) :
    RecyclerView.Adapter<PaymentAdapter.ItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding =
            PaymentListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val payment = items[position]
        holder.paidAtVw.text = payment.payYMDString
        holder.amountVw.text = "${payment.payAmount}"
    }

    override fun getItemCount(): Int = items.size

    class ItemViewHolder(private val binding: PaymentListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val paidAtVw: TextView = binding.paidAtTextView
        val amountVw: TextView = binding.amountTextView
    }
}