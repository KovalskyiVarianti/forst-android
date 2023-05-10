package com.example.forst_android.message.group.domain

import com.example.forst_android.chats.group.list.domain.ChatGroupEntity

interface GetChatGroupByIdUseCase {
    suspend fun getChatGroupById(
        clusterId: String,
        userId: String,
        groupId: String
    ): ChatGroupEntity
}