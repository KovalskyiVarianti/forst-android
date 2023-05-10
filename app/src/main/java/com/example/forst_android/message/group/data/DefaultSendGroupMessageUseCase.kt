package com.example.forst_android.message.group.data

import com.example.forst_android.message.group.domain.SendGroupMessageUseCase
import java.util.UUID
import javax.inject.Inject

class DefaultSendGroupMessageUseCase @Inject constructor(
    private val messageGroupRealtimeDatabase: MessageGroupRealtimeDatabase
) : SendGroupMessageUseCase {
    override fun sendMessage(
        clusterId: String,
        groupId: String,
        userId: String,
        groupMembers: List<String>,
        text: String,
    ) {
        val messageId = UUID.randomUUID().toString()
        val message = MessageGroupRealtimeEntity(
            messageId,
            userId,
            text,
            null,
            System.currentTimeMillis()
        )
        messageGroupRealtimeDatabase.sendMessage(clusterId, groupId, groupMembers, message)
    }
}