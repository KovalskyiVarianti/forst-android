package com.example.forst_android.chats.priv.list.domain

data class ChatEntity(
    val id: String,
    val interlocutorId: String,
    val interlocutorName: String,
    val interlocutorImageUrl: String,
    val topMessageText: String?,
    val topMessageSentTime: Long?,
)
