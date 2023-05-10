package com.example.forst_android.chats.group.list.di

import com.example.forst_android.chats.group.list.data.DefaultChatGroupListenerInteractor
import com.example.forst_android.chats.group.list.domain.ChatGroupListenerInteractor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface InteractorModule {

    @Binds
    fun chatGroupListenerInteractor(
        defaultChatGroupListenerInteractor: DefaultChatGroupListenerInteractor
    ): ChatGroupListenerInteractor
}