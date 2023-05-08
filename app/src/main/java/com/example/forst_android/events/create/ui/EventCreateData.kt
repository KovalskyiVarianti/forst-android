package com.example.forst_android.events.create.ui

data class EventCreateData(
    val name: String,
    val startTime: Long,
    val endTime: Long,
    val type: String,
    val locationName: String,
)
