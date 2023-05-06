package com.example.forst_android.message.priv.data

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class MessageRealtimeEntity(
    val id: String? = null,
    val senderId: String? = null,
    val data: String? = null,
    val type: String? = null,
    val sentTime: Long? = null,
)
