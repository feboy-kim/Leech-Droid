package top.okeep.leech.ui.subjects

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import top.okeep.leech.databinding.SubjectListItemBinding
import top.okeep.leech.models.Subject

class SubjectAdapter(private val items: List<Subject>) :
    RecyclerView.Adapter<SubjectAdapter.ItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = SubjectListItemBinding.inflate(inflater, parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.binding.subject = items[position]
    }

    override fun getItemCount(): Int = items.size

    class ItemViewHolder(val binding: SubjectListItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}