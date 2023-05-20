package com.example.forst_android.message.priv.data

import com.example.forst_android.message.priv.domain.MessagePrivateEntity
import com.example.forst_android.message.priv.domain.MessageListenerInteractor
import com.example.forst_android.message.priv.domain.MessageType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DefaultMessageListenerInteractor @Inject constructor(
    private val messageRealtimeDatabase: MessageRealtimeDatabase
) : MessageListenerInteractor {
    override fun addMessageListener(
        clusterId: String,
        userId: String,
        chatId: String
    ): Flow<List<MessagePrivateEntity>> {
        return messageRealtimeDatabase.addMessageListener(clusterId, userId, chatId)
            .map { messages ->
                messages.map { message ->
                    MessagePrivateEntity(
                        message.id.orEmpty(),
                        message.data.orEmpty(),
                        message.senderId.orEmpty(),
                        message.sentTime!!,
                        try {
                            MessageType.valueOf(message.type.orEmpty())
                        } catch (e: Exception) {
                            MessageType.TEXT
                        },
                    )
                }.sortedBy { it.sendTime }
            }
    }

    override fun removeMessageListener(
        clusterId: String,
        userId: String,
        chatId: String
    ) {
        messageRealtimeDatabase.removeMessageListener(clusterId, userId, chatId)
    }
}