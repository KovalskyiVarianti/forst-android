package com.example.forst_android.message.group.di

import com.example.forst_android.message.group.data.DefaultMessageGroupListenerInteractor
import com.example.forst_android.message.group.domain.MessageGroupListenerInteractor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface InteractorModule {

    @Binds
    fun messageGroupListenerInteractor(
        defaultMessageGroupListenerInteractor: DefaultMessageGroupListenerInteractor
    ): MessageGroupListenerInteractor
}