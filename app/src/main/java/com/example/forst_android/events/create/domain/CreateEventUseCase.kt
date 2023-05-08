package com.example.forst_android.events.create.domain

import com.example.forst_android.events.list.domain.EventEntity

interface CreateEventUseCase {
    fun createEvent(clusterId: String, eventEntity: EventEntity)
}