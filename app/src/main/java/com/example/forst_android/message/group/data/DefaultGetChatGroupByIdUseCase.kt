package com.example.forst_android.message.group.data

import com.example.forst_android.chats.group.create.data.ChatGroupRealtimeDatabase
import com.example.forst_android.chats.group.list.data.ChatGroupEntityMapper
import com.example.forst_android.chats.group.list.domain.ChatGroupEntity
import com.example.forst_android.message.group.domain.GetChatGroupByIdUseCase
import javax.inject.Inject

class DefaultGetChatGroupByIdUseCase @Inject constructor(
    private val chatGroupRealtimeDatabase: ChatGroupRealtimeDatabase,
    private val chatGroupEntityMapper: ChatGroupEntityMapper,
) : GetChatGroupByIdUseCase {
    override suspend fun getChatGroupById(
        clusterId: String,
        userId: String,
        groupId: String
    ): ChatGroupEntity {
        return chatGroupRealtimeDatabase.getGroupById(
            clusterId,
            userId,
            groupId
        ).let { group ->
            chatGroupEntityMapper.map(clusterId, userId, group!!)
        }
    }
}