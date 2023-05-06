package com.example.forst_android.clusters.join.ui.adapter

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.forst_android.common.ui.ItemClickListener

class ClusterJoinAdapter(
    private val itemOnClickListener: ItemClickListener
) : ListAdapter<ClusterJoinItem, ClusterJoinItemHolder>(ClusterItemDiffCallback) {
    private companion object ClusterItemDiffCallback : DiffUtil.ItemCallback<ClusterJoinItem>() {
        override fun areItemsTheSame(
            oldItem: ClusterJoinItem,
            newItem: ClusterJoinItem
        ): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: ClusterJoinItem,
            newItem: ClusterJoinItem
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClusterJoinItemHolder {
        return ClusterJoinItemHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ClusterJoinItemHolder, position: Int) {
        holder.bind(getItem(position), itemOnClickListener)
    }
}