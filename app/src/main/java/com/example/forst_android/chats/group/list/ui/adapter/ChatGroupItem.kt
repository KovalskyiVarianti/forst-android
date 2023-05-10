package com.example.forst_android.chats.group.list.ui.adapter

data class ChatGroupItem(
    val id: String,
    val name: String,
    val imageUrl: String,
    val topMessageSenderName: String,
    val topMessageText: String,
    val topMessageSentTime: String,
)