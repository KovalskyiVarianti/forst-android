package com.example.forst_android.events.list.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.forst_android.databinding.ItemEventsPageBinding
import com.example.forst_android.events.list.ui.adapter.EventListItem
import com.example.forst_android.events.list.ui.adapter.EventsAdapter

class EventsPageHolder(private val binding: ItemEventsPageBinding) : ViewHolder(binding.root) {

    fun bind(events: List<EventListItem>, addEventListener: View.OnClickListener) = binding.apply {
        this.eventList.adapter = EventsAdapter(addEventListener).apply {
            submitList(events)
        }
    }

    companion object {
        fun from(parent: ViewGroup) = EventsPageHolder(
            ItemEventsPageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }
}