package com.example.forst_android.events.list.ui.adapter.holder

import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.ViewGroup
import com.example.forst_android.databinding.ItemAddNewEventBinding

class AddNewEventItemHolder(
    private val binding: ItemAddNewEventBinding
) : EventListItemHolder(binding.root) {

    fun bind(addEventListener: OnClickListener) {
        binding.root.setOnClickListener(addEventListener)
    }

    companion object {
        fun from(parent: ViewGroup) = AddNewEventItemHolder(
            ItemAddNewEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }
}