package com.example.forst_android.events.list.ui

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.forst_android.events.list.ui.adapter.EventListItem

class EventsPageAdapter(
    private val addEventListener : View.OnClickListener
) : RecyclerView.Adapter<EventsPageHolder>() {


    companion object {
        const val PAGE_COUNT = 2
        const val ACTIVE_EVENTS_POSITION = 0
        const val ENDED_EVENTS_POSITION = 1
    }


    private var activeEvents = listOf<EventListItem>()
    private var endedEvents = listOf<EventListItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsPageHolder {
        return EventsPageHolder.from(parent)
    }

    override fun getItemCount() = PAGE_COUNT

    override fun onBindViewHolder(holder: EventsPageHolder, position: Int) {
        when (position) {
            ACTIVE_EVENTS_POSITION -> holder.bind(activeEvents, addEventListener)
            ENDED_EVENTS_POSITION -> holder.bind(endedEvents, addEventListener)
        }
    }

    fun submitPages(eventsPagesItem: EventsPagesItem) {
        activeEvents = eventsPagesItem.activeEvents
        endedEvents = eventsPagesItem.endedEvents
        notifyItemRangeChanged(0, PAGE_COUNT)
    }
}