package com.example.forst_android.message.priv.data

import com.example.forst_android.message.priv.domain.MessageSendUseCase
import java.util.UUID
import javax.inject.Inject

class DefaultMessageSendUseCase @Inject constructor(
    private val messageRealtimeDatabase: MessageRealtimeDatabase,
) : MessageSendUseCase {
    override fun sendMessage(
        clusterId: String,
        chatId: String,
        userId: String,
        interlocutorId: String,
        message: String
    ) {
        val messageId = UUID.randomUUID().toString()
        val messageEntity = MessageRealtimeEntity(
            messageId,
            userId,
            message,
            null,
            System.currentTimeMillis()
        )
        messageRealtimeDatabase.sendMessage(
            clusterId,
            chatId,
            userId,
            interlocutorId,
            messageEntity
        )
    }
}