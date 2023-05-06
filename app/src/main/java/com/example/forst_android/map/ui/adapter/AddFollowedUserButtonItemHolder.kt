package com.example.forst_android.map.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.forst_android.databinding.ItemAddFollowedUserBinding

class AddFollowedUserButtonItemHolder(private val binding: ItemAddFollowedUserBinding) :
    MapFollowedItemHolder(binding.root) {

    fun bind(onAddFollowedUserListener: () -> Unit) = binding.apply {
        root.setOnClickListener { onAddFollowedUserListener() }
    }

    companion object {
        fun from(parent: ViewGroup) = AddFollowedUserButtonItemHolder(
            ItemAddFollowedUserBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }
}