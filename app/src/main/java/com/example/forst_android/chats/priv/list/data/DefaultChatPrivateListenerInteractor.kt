package com.example.forst_android.chats.priv.list.data

import com.example.forst_android.chats.priv.create.data.ChatPrivateRealtimeDatabase
import com.example.forst_android.chats.priv.list.domain.ChatPrivateEntity
import com.example.forst_android.chats.priv.list.domain.ChatPrivateListenerInteractor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DefaultChatPrivateListenerInteractor @Inject constructor(
    private val chatPrivateRealtimeDatabase: ChatPrivateRealtimeDatabase,
    private val chatEntityMapper: ChatEntityMapper,
) : ChatPrivateListenerInteractor {
    override fun addChatListener(clusterId: String, userId: String): Flow<List<ChatPrivateEntity>> {
        return chatPrivateRealtimeDatabase.addChatPrivateListener(
            clusterId, userId,
        ).map { chats ->
            chatEntityMapper.map(clusterId, userId, chats)
        }
    }

    override fun removeChatListener(clusterId: String, userId: String) {
        chatPrivateRealtimeDatabase.removeChatPrivateListener(clusterId, userId)
    }
}