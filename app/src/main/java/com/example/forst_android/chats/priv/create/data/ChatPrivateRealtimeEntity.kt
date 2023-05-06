package com.example.forst_android.chats.priv.create.data

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class ChatPrivateRealtimeEntity(
    val id: String? = null,
    val interlocutorId: String? = null,
    val topMessageId: String? = null,
)