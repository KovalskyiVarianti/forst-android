package com.example.forst_android.events.list.di

import com.example.forst_android.events.list.data.RealtimeEventsListenerInteractor
import com.example.forst_android.events.list.domain.EventsListenerInteractor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface InteractorModule {

    @Binds
    fun eventsListenerInteractor(
        defaultEventsListenerInteractor: RealtimeEventsListenerInteractor
    ): EventsListenerInteractor
}