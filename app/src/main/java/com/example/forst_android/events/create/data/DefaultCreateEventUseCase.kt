package com.example.forst_android.events.create.data

import com.example.forst_android.events.create.domain.CreateEventUseCase
import com.example.forst_android.events.list.data.EventRealtimeDatabase
import com.example.forst_android.events.list.data.EventRealtimeEntity
import com.example.forst_android.events.list.domain.EventEntity
import javax.inject.Inject

class DefaultCreateEventUseCase @Inject constructor(
    private val eventRealtimeDatabase: EventRealtimeDatabase,
) : CreateEventUseCase {
    override fun createEvent(clusterId: String, eventEntity: EventEntity) {
        eventRealtimeDatabase.createEvent(
            clusterId,
            EventRealtimeEntity(
                eventEntity.id,
                eventEntity.name,
                eventEntity.startTime,
                eventEntity.endTime,
                eventEntity.type,
                eventEntity.locationName,
                eventEntity.location
            )
        )
    }
}