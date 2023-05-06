package com.example.forst_android.chats.priv.create.di

import com.example.forst_android.chats.priv.create.data.DefaultGetChatPrivateIdUseCase
import com.example.forst_android.chats.priv.create.domain.GetChatPrivateIdUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface UseCaseModule {

    @Binds
    fun getChatPrivateIdUseCase(
        defaultGetChatPrivateIdUseCase: DefaultGetChatPrivateIdUseCase
    ): GetChatPrivateIdUseCase
}