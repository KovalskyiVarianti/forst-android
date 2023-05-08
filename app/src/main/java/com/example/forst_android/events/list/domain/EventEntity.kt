package com.example.forst_android.events.list.domain

data class EventEntity(
    val id: String,
    val name: String,
    val startTime: Long,
    val endTime: Long,
    val type: String,
    val locationName: String,
    val location: String,
)
