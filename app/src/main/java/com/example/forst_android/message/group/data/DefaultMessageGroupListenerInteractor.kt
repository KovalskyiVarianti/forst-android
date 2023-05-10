package com.example.forst_android.message.group.data

import com.example.forst_android.message.group.domain.MessageGroupEntity
import com.example.forst_android.message.group.domain.MessageGroupListenerInteractor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DefaultMessageGroupListenerInteractor @Inject constructor(
    private val messageGroupRealtimeDatabase: MessageGroupRealtimeDatabase,
) : MessageGroupListenerInteractor {
    override fun addMessageListener(
        clusterId: String,
        userId: String,
        groupId: String
    ): Flow<List<MessageGroupEntity>> {
        return messageGroupRealtimeDatabase.addMessageListener(
            clusterId,
            userId,
            groupId
        ).map { messages ->
            messages.map { message ->
                MessageGroupEntity(
                    message.id.orEmpty(),
                    message.data.orEmpty(),
                    message.senderId.orEmpty(),
                    message.sentTime!!,
                    message.type.orEmpty()
                )
            }
        }
    }

    override fun removeMessageListener(clusterId: String, userId: String, groupId: String) {
        messageGroupRealtimeDatabase.removeMessageListener(clusterId, userId, groupId)
    }
}