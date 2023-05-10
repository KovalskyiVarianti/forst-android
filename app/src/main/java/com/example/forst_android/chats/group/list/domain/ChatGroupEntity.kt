package com.example.forst_android.chats.group.list.domain

data class ChatGroupEntity(
    val id: String,
    val name: String,
    val members: List<String>,
    val topMessageSenderName: String,
    val topMessageText: String?,
    val topMessageSentTime: Long?,
)
