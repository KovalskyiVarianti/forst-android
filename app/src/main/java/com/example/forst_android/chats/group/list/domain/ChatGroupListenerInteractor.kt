package com.example.forst_android.chats.group.list.domain

import kotlinx.coroutines.flow.Flow

interface ChatGroupListenerInteractor {
    fun addChatListener(clusterId: String, userId:String): Flow<List<ChatGroupEntity>>
    fun removeChatListener(clusterId: String, userId:String)
}