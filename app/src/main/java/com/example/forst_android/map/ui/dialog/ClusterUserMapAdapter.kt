package com.example.forst_android.map.ui.dialog

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

class ClusterUserMapAdapter(
    private val onFollow: (userId: String, isFollowed: Boolean) -> Unit
) : ListAdapter<ClusterUserMapItem, ClusterUserMapItemHolder>(ClusterUserMapItemDiffCallback) {

    private companion object ClusterUserMapItemDiffCallback :
        DiffUtil.ItemCallback<ClusterUserMapItem>() {
        override fun areItemsTheSame(
            oldItem: ClusterUserMapItem,
            newItem: ClusterUserMapItem
        ): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: ClusterUserMapItem,
            newItem: ClusterUserMapItem
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClusterUserMapItemHolder {
        return ClusterUserMapItemHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ClusterUserMapItemHolder, position: Int) {
        holder.bind(getItem(position), onFollow)
    }
}