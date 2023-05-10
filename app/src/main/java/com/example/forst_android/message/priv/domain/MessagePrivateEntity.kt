package com.example.forst_android.message.priv.domain

data class MessagePrivateEntity(
    val id: String,
    val data: String,
    val senderId: String,
    val sendTime: Long,
    val type: String
)