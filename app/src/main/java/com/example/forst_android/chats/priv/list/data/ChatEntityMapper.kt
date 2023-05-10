package com.example.forst_android.chats.priv.list.data

import com.example.forst_android.chats.priv.create.data.ChatPrivateRealtimeEntity
import com.example.forst_android.chats.priv.list.domain.ChatPrivateEntity
import com.example.forst_android.common.data.database.UserRealtimeDatabase
import com.example.forst_android.message.priv.data.MessageRealtimeDatabase
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
            ChatPrivateEntity(
                chat.id.orEmpty(),
                interlocutor?.id.orEmpty(),
                interlocutor?.name ?: interlocutor?.phoneNumber.orEmpty(),
                interlocutor?.photoUri.orEmpty(),
                topMessage?.data,
                topMessage?.sentTime,
            )
        }.sortedByDescending { it.topMessageSentTime }
    }
}