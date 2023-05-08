package com.example.forst_android.events.create.ui

import com.example.forst_android.events.list.domain.EventEntity
import java.util.*
import javax.inject.Inject

class EventCreateEntityMapper @Inject constructor() {

    fun map(eventCreateData: EventCreateData): EventEntity {
        val eventId = UUID.randomUUID().toString()
        return with(eventCreateData) {
            EventEntity(eventId, name, startTime, endTime, type, locationName, locationName)
        }
    }
}