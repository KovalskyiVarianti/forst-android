package com.example.forst_android.chats.group.list.ui.adapter

import com.example.forst_android.message.priv.domain.MessageType

data class ChatGroupItem(
    val id: String,
    val name: String,
    val topMessageSenderName: String,
    val topMessageSenderImage: String,
    val topMessageText: String,
    val topMessageType: MessageType,
    val topMessageSentTime: String,
)