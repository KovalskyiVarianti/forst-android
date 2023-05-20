package com.example.forst_android.events.list.data

import com.example.forst_android.events.list.domain.EventEntity
import com.example.forst_android.events.list.domain.EventsListenerInteractor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RealtimeEventsListenerInteractor @Inject constructor(
    private val eventRealtimeDatabase: EventRealtimeDatabase
) : EventsListenerInteractor {
    override fun addEventsListener(clusterId: String): Flow<List<EventEntity>> {
        return eventRealtimeDatabase.addEventListener(clusterId).map { events ->
            events.map { event ->
                EventEntity(
                    event.id.orEmpty(),
                    event.name.orEmpty(),
                    event.startTime!!,
                    event.endTime!!,
                    event.type.orEmpty(),
                    event.locationName.orEmpty(),
                    event.location.orEmpty()
                )
            }
        }
    }

    override fun removeEventsListener() {
        eventRealtimeDatabase.removeEventListener()
    }
}