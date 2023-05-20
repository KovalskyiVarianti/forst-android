package com.example.forst_android.chats.priv.list.ui.adapter

import com.example.forst_android.message.priv.domain.MessageType

data class ChatPrivateItem(
    val id: String,
    val personUID: String,
    val personName: String,
    val personPhoto: String,
    val topMessageText: String,
    val topMessageType: MessageType,
    val topMessageSentTime: String,
)
