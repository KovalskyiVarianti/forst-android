package com.example.forst_android.chats.group.create.data

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class ChatGroupRealtimeEntity(
    val id: String? = null,
    val name: String? = null,
    val members: Map<String, Any>? = null,
    val topMessageId: String? = null,
)