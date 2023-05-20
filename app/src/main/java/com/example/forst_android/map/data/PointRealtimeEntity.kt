package com.example.forst_android.map.data

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class PointRealtimeEntity(
    val id: String? = null,
    val name: String? = null,
    val lat: Double? = null,
    val lng: Double? = null,
    val creatorId: String? = null,
    val createTime: Long? = null,
)
