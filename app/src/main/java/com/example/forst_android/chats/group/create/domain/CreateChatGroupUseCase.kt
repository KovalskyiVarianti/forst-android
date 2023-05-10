package com.example.forst_android.chats.group.create.domain

interface CreateChatGroupUseCase {
    fun createGroup(
        clusterId: String,
        creatorId: String,
        name: String,
        membersIds: List<String>
    ): String
}