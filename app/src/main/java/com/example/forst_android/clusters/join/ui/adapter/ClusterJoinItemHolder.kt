package com.example.forst_android.clusters.join.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.forst_android.common.ui.ItemClickListener
import com.example.forst_android.databinding.ItemClusterJoinBinding

class ClusterJoinItemHolder(private val binding: ItemClusterJoinBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: ClusterJoinItem, itemOnClickListener: ItemClickListener) {
        binding.clusterNameTitle.text = "${item.name} ${item.type.name}"
        binding.root.setOnClickListener { itemOnClickListener(item.id) }
    }

    companion object {
        fun from(parent: ViewGroup) = ClusterJoinItemHolder(
            ItemClusterJoinBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }
}