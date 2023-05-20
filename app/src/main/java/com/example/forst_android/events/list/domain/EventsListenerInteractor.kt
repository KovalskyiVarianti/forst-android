package com.example.forst_android.events.list.domain

import kotlinx.coroutines.flow.Flow

interface EventsListenerInteractor {
    fun addEventsListener(clusterId : String) : Flow<List<EventEntity>>
    fun removeEventsListener()
}