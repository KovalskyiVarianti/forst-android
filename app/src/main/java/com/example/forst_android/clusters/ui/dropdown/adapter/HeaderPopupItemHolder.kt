package com.example.forst_android.clusters.ui.dropdown.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.forst_android.databinding.ItemHeaderPopupBinding

class HeaderPopupItemHolder(
    private val binding: ItemHeaderPopupBinding
) : ClusterPopupHolder(binding.root) {

    fun bind(item: ClusterPopupItem.HeaderItem) {
        binding.headerTitle.text = item.text
    }

    companion object {
        fun from(parent: ViewGroup) = HeaderPopupItemHolder(
            ItemHeaderPopupBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }
}