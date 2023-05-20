package com.example.forst_android.clusters.join.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.forst_android.R
import com.example.forst_android.clusters.join.ui.adapter.ClusterJoinItem.ClusterJoinType.*
import com.example.forst_android.common.ui.ItemClickListener
import com.example.forst_android.databinding.ItemClusterJoinBinding

class ClusterJoinItemHolder(private val binding: ItemClusterJoinBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: ClusterJoinItem, itemOnClickListener: ItemClickListener) = binding.apply {
        clusterNameTitle.text = item.name
        when (item.type) {
            SELF -> {
                clusterState.text = root.context.getString(R.string.my)
                clusterCard.setCardBackgroundColor(root.context.getColor(R.color.primary))
                root.setOnClickListener { }
            }
            JOINED -> {
                clusterState.text = root.context.getString(R.string.joined)
                clusterCard.setCardBackgroundColor(root.context.getColor(R.color.primary))
                root.setOnClickListener { }
            }
            NOT_JOINED -> {
                clusterState.text = root.context.getString(R.string.not_joined)
                clusterCard.setCardBackgroundColor(root.context.getColor(R.color.surface))
                root.setOnClickListener { itemOnClickListener(item.id) }
            }
        }
    }

    companion object {
        fun from(parent: ViewGroup) = ClusterJoinItemHolder(
            ItemClusterJoinBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }
}