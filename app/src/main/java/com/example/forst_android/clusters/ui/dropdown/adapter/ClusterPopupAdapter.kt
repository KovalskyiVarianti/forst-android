package com.example.forst_android.clusters.ui.dropdown.adapter

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.forst_android.common.ui.ItemClickListener

class ClusterPopupAdapter(
    private val itemOnClickListener: ItemClickListener
) : ListAdapter<ClusterPopupItem, ClusterPopupHolder>(ClusterItemDiffCallback) {
    private companion object ClusterItemDiffCallback : DiffUtil.ItemCallback<ClusterPopupItem>() {
        override fun areItemsTheSame(
            oldItem: ClusterPopupItem,
            newItem: ClusterPopupItem
        ): Boolean {
            return if (oldItem is ClusterPopupItem.ClusterItem && newItem is ClusterPopupItem.ClusterItem) {
                oldItem.id == newItem.id
            } else {
                oldItem == newItem
            }
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: ClusterPopupItem,
            newItem: ClusterPopupItem
        ): Boolean {
            return oldItem == newItem
        }
    }

    enum class ViewType(val type: Int) {
        CLUSTER(0), HEADER(1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClusterPopupHolder {
        return when (ViewType.values().first { it.type == viewType }) {
            ViewType.CLUSTER -> ClusterPopupItemHolder.from(parent)
            ViewType.HEADER -> HeaderPopupItemHolder.from(parent)
        }
    }

    override fun onBindViewHolder(holder: ClusterPopupHolder, position: Int) {
        when (holder) {
            is ClusterPopupItemHolder -> holder.bind(
                getItem(position) as ClusterPopupItem.ClusterItem,
                itemOnClickListener
            )
            is HeaderPopupItemHolder -> holder.bind(
                getItem(position) as ClusterPopupItem.HeaderItem
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is ClusterPopupItem.ClusterItem -> ViewType.CLUSTER.type
            is ClusterPopupItem.HeaderItem -> ViewType.HEADER.type
        }
    }
}