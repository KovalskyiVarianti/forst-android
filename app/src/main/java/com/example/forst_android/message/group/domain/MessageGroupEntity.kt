package com.example.forst_android.message.group.domain

data class MessageGroupEntity(
    val id: String,
    val data: String,
    val senderId: String,
    val sendTime: Long,
    val type: String
)