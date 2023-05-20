package com.example.forst_android.events.list.ui

import com.example.forst_android.events.list.ui.adapter.EventListItem

data class EventsPagesItem(
    val activeEvents : List<EventListItem>,
    val endedEvents: List<EventListItem>,
)
