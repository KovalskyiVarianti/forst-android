package com.example.forst_android.events.create.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.forst_android.databinding.ItemPointBinding

class PointAdapter(
    private val pointClickListener: (point: PointItem) -> Unit
) :
    ListAdapter<PointItem, PointItemHolder>(PointItemDiffCallback) {
    companion object PointItemDiffCallback : DiffUtil.ItemCallback<PointItem>() {
        override fun areItemsTheSame(oldItem: PointItem, newItem: PointItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PointItem, newItem: PointItem): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PointItemHolder {
        return PointItemHolder.from(parent)
    }

    override fun onBindViewHolder(holder: PointItemHolder, position: Int) {
        holder.bind(getItem(position), pointClickListener)
    }
}

data class PointItem(
    val id: String,
    val name: String,
)

class PointItemHolder(private val binding: ItemPointBinding) : ViewHolder(binding.root) {

    fun bind(item: PointItem, pointClickListener: (item: PointItem) -> Unit) = binding.apply {
        pointName.text = item.name
        root.setOnClickListener { pointClickListener(item) }
    }

    companion object {
        fun from(parent: ViewGroup) = PointItemHolder(
            ItemPointBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
}