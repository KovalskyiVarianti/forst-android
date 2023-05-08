package com.example.forst_android.events.create.di

import com.example.forst_android.events.create.data.DefaultCreateEventUseCase
import com.example.forst_android.events.create.domain.CreateEventUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface UseCaseModule {

    @Binds
    fun createEventUseCase(
        defaultCreateEventUseCase: DefaultCreateEventUseCase
    ) : CreateEventUseCase
}