package com.example.forst_android.message.group.di

import com.example.forst_android.message.group.data.DefaultGetChatGroupByIdUseCase
import com.example.forst_android.message.group.data.DefaultSendGroupMessageUseCase
import com.example.forst_android.message.group.domain.GetChatGroupByIdUseCase
import com.example.forst_android.message.group.domain.SendGroupMessageUseCase
import com.example.forst_android.message.priv.data.DefaultGetUserByIdUseCase
import com.example.forst_android.message.priv.data.DefaultMessageSendUseCase
import com.example.forst_android.message.priv.domain.GetUserByIdUseCase
import com.example.forst_android.message.priv.domain.MessageSendUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface UseCaseModule {

    @Binds
    fun sendGroupMessageUseCase(
        defaultSendGroupMessageUseCase: DefaultSendGroupMessageUseCase
    ): SendGroupMessageUseCase

    @Binds
    fun getChatGroupByIdUseCase(
        defaultGetChatGroupByIdUseCase: DefaultGetChatGroupByIdUseCase
    ): GetChatGroupByIdUseCase
}