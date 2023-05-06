package com.example.forst_android.chats.priv.create.domain

interface GetChatPrivateIdUseCase {
    suspend fun getChatId(interlocutorId: String) : String
}