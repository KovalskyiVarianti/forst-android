package com.example.forst_android.chats.priv.list.domain

import kotlinx.coroutines.flow.Flow

interface ChatPrivateListenerInteractor {
    fun addChatListener(clusterId: String, userId: String): Flow<List<ChatPrivateEntity>>
    fun removeChatListener(clusterId: String, userId: String)
}