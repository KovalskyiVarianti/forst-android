package com.example.forst_android.chats.group.list.domain

import com.example.forst_android.message.priv.domain.MessageType

data class ChatGroupEntity(
    val id: String,
    val name: String,
    val members: List<String>,
    val topMessageSenderName: String,
    val topMessageSenderImage: String,
    val topMessageText: String?,
    val topMessageType: MessageType,
    val topMessageSentTime: Long?,
)
