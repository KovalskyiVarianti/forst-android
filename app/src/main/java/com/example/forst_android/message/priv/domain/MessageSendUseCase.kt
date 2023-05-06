package com.example.forst_android.message.priv.domain

interface MessageSendUseCase {
    fun sendMessage(
        clusterId: String,
        chatId: String,
        userId: String,
        interlocutorId: String,
        message: String,
    )
}