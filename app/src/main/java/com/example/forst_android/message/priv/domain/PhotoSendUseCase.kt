package com.example.forst_android.message.priv.domain

import java.io.InputStream

interface PhotoSendUseCase {
    suspend fun sendPhoto(
        clusterId: String,
        chatId: String,
        userId: String,
        interlocutorId: String,
        inputStream: InputStream,
    )
}