package com.example.forst_android.events.list.data

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class EventRealtimeEntity(
    val id: String? = null,
    val name: String? = null,
    val startTime: Long? = null,
    val endTime: Long? = null,
    val type: String? = null,
    val locationName: String? = null,
    val location: String? = null,
)