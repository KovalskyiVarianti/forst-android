package com.example.forst_android.message.group.data

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class MessageGroupRealtimeEntity(
    val id: String? = null,
    val senderId: String? = null,
    val data: String? = null,
    val type: String? = null,
    val sentTime: Long? = null,
)