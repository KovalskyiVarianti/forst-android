package com.example.forst_android.events.list.ui

import com.example.forst_android.events.list.domain.EventEntity
import com.example.forst_android.events.list.ui.adapter.EventListItem
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class EventListItemMapper @Inject constructor() {

    private val timeFormatter: SimpleDateFormat by lazy {
        SimpleDateFormat("HH:mm", Locale.getDefault())
    }

    private val dateFormatter: SimpleDateFormat by lazy {
        SimpleDateFormat("EEEE dd/MM/yyyy", Locale.getDefault())
    }

    fun map(eventEntities: List<EventEntity>): List<EventListItem> {
        val sortedEventListItems = mutableListOf<EventListItem>()
        eventEntities.groupBy {
            it.startTime
        }.toSortedMap().forEach { (date, events) ->
            sortedEventListItems.add(EventListItem.EventDateItem(dateFormatter.format(date)))
            sortedEventListItems.addAll(
                events.map { eventEntity ->
                    EventListItem.EventItem(
                        eventEntity.id,
                        eventEntity.name,
                        getEventTime(eventEntity.startTime, eventEntity.endTime),
                        eventEntity.type,
                        eventEntity.locationName,
                        eventEntity.location
                    )
                }
            )
        }
        return listOf(EventListItem.AddNewEventItem) + sortedEventListItems.distinct()
    }

    private fun getEventTime(startTime: Long, endTime: Long): String {
        val startTimeFormatted = timeFormatter.format(startTime)
        val endTimeFormatted = timeFormatter.format(endTime)
        return "$startTimeFormatted - $endTimeFormatted"
    }
}