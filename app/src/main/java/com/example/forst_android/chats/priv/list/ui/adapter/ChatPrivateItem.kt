package com.example.forst_android.chats.priv.list.ui.adapter

data class ChatPrivateItem(
    val id: String,
    val personUID: String,
    val personName: String,
    val personPhoto: String,
    val topMessageText: String,
    val topMessageSentTime: String,
)
