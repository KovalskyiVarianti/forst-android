package com.example.forst_android.clusters.ui.dropdown

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

class ClusterPopupAdapter(
    private val itemOnClickListener: ClusterDropdownOnSelectedListener
) : ListAdapter<ClusterPopupItem, ClusterPopupItemHolder>(ClusterItemDiffCallback) {
    private companion object ClusterItemDiffCallback : DiffUtil.ItemCallback<ClusterPopupItem>() {
        override fun areItemsTheSame(
            oldItem: ClusterPopupItem,
            newItem: ClusterPopupItem
        ): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: ClusterPopupItem,
            newItem: ClusterPopupItem
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClusterPopupItemHolder {
        return ClusterPopupItemHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ClusterPopupItemHolder, position: Int) {
        holder.bind(getItem(position), itemOnClickListener)
    }
}