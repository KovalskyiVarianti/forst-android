package com.example.forst_android.chats.priv.list.data

import com.example.forst_android.chats.priv.create.data.ChatPrivateRealtimeEntity
import com.example.forst_android.chats.priv.list.domain.ChatPrivateEntity
import com.example.forst_android.common.data.database.UserRealtimeDatabase
import com.example.forst_android.common.domain.service.UserService
import com.example.forst_android.message.priv.data.MessageRealtimeDatabase
import com.example.forst_android.message.priv.domain.MessageType
import javax.inject.Inject

class ChatEntityMapper @Inject constructor(
    private val userRealtimeDatabase: UserRealtimeDatabase,
    private val messageRealtimeDatabase: MessageRealtimeDatabase,
) {
    suspend fun map(
        clusterId: String,
        userId: String,
        chatPrivateRealtimeEntities: List<ChatPrivateRealtimeEntity>
    ): List<ChatPrivateEntity> {
        return chatPrivateRealtimeEntities.map { chat ->
            val interlocutor = userRealtimeDatabase.getUserById(chat.interlocutorId.orEmpty())
            val topMessage = chat.topMessageId?.let {
                messageRealtimeDatabase.getMessageById(clusterId, userId, chat.id.orEmpty(), it)
            }
            val interlocutorId = interlocutor?.id.orEmpty()
            ChatPrivateEntity(
                chat.id.orEmpty(),
                interlocutorId,
                interlocutor?.name ?: interlocutor?.phoneNumber.orEmpty(),
                UserService.getPhotoUrl(interlocutorId),
                topMessage?.data,
                try {
                    MessageType.valueOf(topMessage?.type.orEmpty())
                } catch (e: Exception) {
                    MessageType.TEXT
                },
                topMessage?.sentTime,
            )
        }.sortedByDescending { it.topMessageSentTime }
    }
}