package com.example.forst_android.message.group.domain

import com.example.forst_android.message.priv.domain.MessageType

data class MessageGroupEntity(
    val id: String,
    val data: String,
    val senderId: String,
    val sendTime: Long,
    val type: MessageType
)