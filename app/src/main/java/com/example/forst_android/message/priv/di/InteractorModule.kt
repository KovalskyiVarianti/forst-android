package com.example.forst_android.message.priv.di

import com.example.forst_android.message.priv.data.DefaultMessageListenerInteractor
import com.example.forst_android.message.priv.domain.MessageListenerInteractor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface InteractorModule {

    @Binds
    fun messageListenerInteractor(
        defaultMessageListenerInteractor: DefaultMessageListenerInteractor
    ): MessageListenerInteractor
}