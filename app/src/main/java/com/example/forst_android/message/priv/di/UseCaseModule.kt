package com.example.forst_android.message.priv.di

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
    fun messageSendUseCase(
        defaultMessageSendUseCase: DefaultMessageSendUseCase
    ) : MessageSendUseCase

    @Binds
    fun getUserByIdUseCase(
        defaultGetUseByIdUseCase: DefaultGetUserByIdUseCase
    ) : GetUserByIdUseCase
}