package com.example.forst_android.chats.priv.list.domain

import com.example.forst_android.message.priv.domain.MessageType

data class ChatPrivateEntity(
    val id: String,
    val interlocutorId: String,
    val interlocutorName: String,
    val interlocutorImageUrl: String,
    val topMessageText: String?,
    val topMessageType: MessageType,
    val topMessageSentTime: Long?,
)
