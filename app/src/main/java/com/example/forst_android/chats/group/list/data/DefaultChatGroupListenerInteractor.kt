package com.example.forst_android.chats.group.list.data

import com.example.forst_android.chats.group.create.data.ChatGroupRealtimeDatabase
import com.example.forst_android.chats.group.list.domain.ChatGroupEntity
import com.example.forst_android.chats.group.list.domain.ChatGroupListenerInteractor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DefaultChatGroupListenerInteractor @Inject constructor(
    private val chatGroupRealtimeDatabase: ChatGroupRealtimeDatabase,
    private val chatGroupEntityMapper: ChatGroupEntityMapper,
) : ChatGroupListenerInteractor {
    override fun addChatListener(clusterId: String, userId: String): Flow<List<ChatGroupEntity>> {
        return chatGroupRealtimeDatabase.addChatGroupListener(clusterId, userId).map { groups ->
            groups.map { group ->
                chatGroupEntityMapper.map(clusterId, userId, group)
            }
        }
    }

    override fun removeChatListener(clusterId: String, userId: String) {
        chatGroupRealtimeDatabase.removeChatGroupListener(clusterId, userId)
    }
}