package com.example.forst_android.chats.priv.list.di

import com.example.forst_android.chats.priv.list.data.DefaultChatPrivateListenerInteractor
import com.example.forst_android.chats.priv.list.domain.ChatPrivateListenerInteractor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface InteractorModule {

    @Binds
    fun chatPrivateListenerInteractor(
        defaultChatPrivateListenerInteractor: DefaultChatPrivateListenerInteractor
    ): ChatPrivateListenerInteractor
}