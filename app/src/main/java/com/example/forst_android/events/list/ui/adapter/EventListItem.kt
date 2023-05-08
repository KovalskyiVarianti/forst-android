package com.example.forst_android.events.list.ui.adapter

sealed interface EventListItem {
    object AddNewEventItem : EventListItem

    data class EventDateItem(val date: String) : EventListItem

    data class EventItem(
        val id: String,
        val name: String,
        val eventTime: String,
        val type: String,
        val locationName: String,
        val location: String,
    ) : EventListItem
}