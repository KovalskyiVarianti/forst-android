package com.example.forst_android.message.priv.domain

import kotlinx.coroutines.flow.Flow

interface MessageListenerInteractor {
    fun addMessageListener(
        clusterId: String,
        userId: String,
        chatId: String
    ): Flow<List<MessageEntity>>

    fun removeMessageListener(
        clusterId: String,
        userId: String,
        chatId: String
    )
}