package com.example.forst_android.chats.priv.create.data

import com.example.forst_android.chats.priv.create.domain.GetChatPrivateIdUseCase
import com.example.forst_android.clusters.data.ClusterPreferences
import com.example.forst_android.common.domain.service.UserService
import kotlinx.coroutines.flow.firstOrNull
import java.util.*
import javax.inject.Inject

class DefaultGetChatPrivateIdUseCase @Inject constructor(
    private val userService: UserService,
    private val chatPrivateRealtimeDatabase: ChatPrivateRealtimeDatabase,
    private val clusterPreferences: ClusterPreferences,
) : GetChatPrivateIdUseCase {
    override suspend fun getChatId(interlocutorId: String): String {
        val clusterId = clusterPreferences.getSelectedClusterId().firstOrNull()
            ?: throw IllegalArgumentException()
        val userId = userService.userUID ?: throw IllegalArgumentException()
        return chatPrivateRealtimeDatabase.getPrivateChatId(
            clusterId,
            userId,
            interlocutorId
        ) ?: kotlin.run {
            val chatId = UUID.randomUUID().toString()
            chatPrivateRealtimeDatabase.createPrivateChat(clusterId, chatId, userId, interlocutorId)
            chatId
        }
    }
}