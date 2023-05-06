package com.example.forst_android.home.di

import com.example.forst_android.home.data.DefaultClusterJoinedListenerInteractor
import com.example.forst_android.home.domain.ClusterJoinedListenerInteractor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface InteractorModule {

    @Binds
    fun clusterJoinedListenerInteractor(
        defaultClusterJoinedListenerInteractor: DefaultClusterJoinedListenerInteractor
    ): ClusterJoinedListenerInteractor
}