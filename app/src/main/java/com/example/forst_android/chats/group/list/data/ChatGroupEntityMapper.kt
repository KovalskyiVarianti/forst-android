package com.example.forst_android.chats.group.list.data

import com.example.forst_android.chats.group.create.data.ChatGroupRealtimeEntity
import com.example.forst_android.chats.group.list.domain.ChatGroupEntity
import com.example.forst_android.common.data.database.UserRealtimeDatabase
import com.example.forst_android.common.domain.service.UserService
import com.example.forst_android.message.group.data.MessageGroupRealtimeDatabase
import com.example.forst_android.message.priv.domain.MessageType
import javax.inject.Inject

class ChatGroupEntityMapper @Inject constructor(
    private val messageGroupRealtimeDatabase: MessageGroupRealtimeDatabase,
    private val userRealtimeDatabase: UserRealtimeDatabase,
) {
    suspend fun map(
        clusterId: String,
        userId: String,
        chatGroup: ChatGroupRealtimeEntity
    ): ChatGroupEntity {
        val groupId = chatGroup.id.orEmpty()
        val message = messageGroupRealtimeDatabase.getMessageById(
            clusterId,
            userId,
            groupId,
            chatGroup.topMessageId.orEmpty()
        )
        val user = userRealtimeDatabase.getUserById(message?.senderId.orEmpty())
        return ChatGroupEntity(
            groupId,
            chatGroup.name.orEmpty(),
            chatGroup.members?.keys?.toList().orEmpty(),
            user?.name ?: user?.phoneNumber.orEmpty(),
            UserService.getPhotoUrl(user?.id.orEmpty()),
            message?.data,
            try {
                MessageType.valueOf(message?.type.orEmpty())
            } catch (e: Exception) {
                MessageType.TEXT
            },
            message?.sentTime
        )
    }
}