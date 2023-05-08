package com.example.forst_android.events.list.ui.adapter.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.forst_android.databinding.ItemEventBinding
import com.example.forst_android.events.list.ui.adapter.EventListItem

class EventItemHolder(private val binding: ItemEventBinding) : EventListItemHolder(binding.root) {

    fun bind(item: EventListItem.EventItem) = binding.apply {
        eventName.text = item.name
        eventTime.text = item.eventTime
        eventType.text = item.type
        eventLocationName.text = item.locationName
        eventLocation.text = item.location
    }

    companion object {
        fun from(parent: ViewGroup) = EventItemHolder(
            ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }
}