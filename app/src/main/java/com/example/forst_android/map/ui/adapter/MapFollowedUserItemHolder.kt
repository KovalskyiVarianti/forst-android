package com.example.forst_android.map.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.forst_android.common.ui.ItemClickListener
import com.example.forst_android.databinding.ItemFollowedUserBinding

class MapFollowedUserItemHolder(private val binding: ItemFollowedUserBinding) :
    MapFollowedItemHolder(binding.root) {

    fun bind(
        item: MapFollowedItem.UserMapFollowedItem,
        itemClickListener: ItemClickListener
    ) = binding.apply {
        root.setOnClickListener {
            itemClickListener(item.id)
        }
        userName.text = item.name
    }

    companion object {
        fun from(parent: ViewGroup) = MapFollowedUserItemHolder(
            ItemFollowedUserBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }
}