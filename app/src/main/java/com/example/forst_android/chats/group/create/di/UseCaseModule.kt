package com.example.forst_android.chats.group.create.di

import com.example.forst_android.chats.group.create.data.DefaultCreateChatGroupUseCase
import com.example.forst_android.chats.group.create.domain.CreateChatGroupUseCase
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
    fun createChatGroupUseCase(
        defaultCreateChatGroupUseCase: DefaultCreateChatGroupUseCase
    ): CreateChatGroupUseCase
}