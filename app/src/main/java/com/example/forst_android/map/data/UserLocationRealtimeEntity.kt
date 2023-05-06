package com.example.forst_android.map.data

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class UserLocationRealtimeEntity(
    val id: String? = null,
    val lat: Double? = null,
    val lng: Double? = null,
    val enabled: Boolean? = null,
    val lastUpdate: Long? = null,
)
