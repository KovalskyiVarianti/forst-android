package com.example.forst_android.chats.group.create.data

import com.example.forst_android.chats.group.create.domain.CreateChatGroupUseCase
import java.util.UUID
import javax.inject.Inject

class DefaultCreateChatGroupUseCase @Inject constructor(
    private val chatGroupRealtimeDatabase: ChatGroupRealtimeDatabase,
) : CreateChatGroupUseCase {
    override fun createGroup(
        clusterId: String,
        creatorId: String,
        name: String,
        membersIds: List<String>
    ): String {
        val groupId = UUID.randomUUID().toString()
        chatGroupRealtimeDatabase.createGroupChat(clusterId, groupId, name, creatorId, membersIds)
        return groupId
    }
}