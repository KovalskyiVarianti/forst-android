package com.example.forst_android.message.group.domain

interface SendGroupMessageUseCase {
    fun sendMessage(
        clusterId: String,
        groupId: String,
        userId: String,
        groupMembers: List<String>,
        text: String,
    )
}