package com.example.forst_android.events.list.ui.adapter.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.forst_android.databinding.ItemEventDateBinding
import com.example.forst_android.events.list.ui.adapter.EventListItem

class DateItemHolder(
    private val binding: ItemEventDateBinding
) : EventListItemHolder(binding.root) {

    fun bind(item: EventListItem.EventDateItem) = binding.apply {
        eventDateText.text = item.date
    }

    companion object {
        fun from(parent: ViewGroup) = DateItemHolder(
            ItemEventDateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }
}