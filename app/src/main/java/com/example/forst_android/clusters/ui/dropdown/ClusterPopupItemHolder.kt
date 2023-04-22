package com.example.forst_android.clusters.ui.dropdown

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.forst_android.common.ui.ItemClickListener
import com.example.forst_android.databinding.ItemClusterPopupBinding

class ClusterPopupItemHolder(private val binding: ItemClusterPopupBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: ClusterPopupItem, itemOnClickListener: ItemClickListener) {
        binding.clusterNameTitle.text = item.name
        binding.root.setOnClickListener { itemOnClickListener(item.id) }
    }

    companion object {
        fun from(parent: ViewGroup) = ClusterPopupItemHolder(
            ItemClusterPopupBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }
}