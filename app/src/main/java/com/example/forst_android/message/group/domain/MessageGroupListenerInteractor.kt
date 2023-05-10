package com.example.forst_android.message.group.domain

import kotlinx.coroutines.flow.Flow

interface MessageGroupListenerInteractor {
    fun addMessageListener(
        clusterId: String,
        userId: String,
        groupId: String
    ): Flow<List<MessageGroupEntity>>

    fun removeMessageListener(
        clusterId: String,
        userId: String,
        groupId: String
    )
}