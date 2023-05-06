package com.example.forst_android.message.priv.data

import com.example.forst_android.message.priv.domain.MessageEntity
import com.example.forst_android.message.priv.domain.MessageListenerInteractor
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
    ): Flow<List<MessageEntity>> {
        return messageRealtimeDatabase.addMessageListener(clusterId, userId, chatId)
            .map { messages ->
                messages.map { message ->
                    MessageEntity(
                        message.id.orEmpty(),
                        message.data.orEmpty(),
                        message.senderId.orEmpty(),
                        message.sentTime!!,
                        message.type.orEmpty(),
                    )
                }
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