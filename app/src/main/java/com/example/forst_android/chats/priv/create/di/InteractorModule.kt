package com.example.forst_android.chats.priv.create.di

import com.example.forst_android.chats.priv.create.data.DefaultClusterMembersListenerInteractor
import com.example.forst_android.chats.priv.create.domain.ClusterMembersListenerInteractor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface InteractorModule {

    @Binds
    fun clusterMembersListenerInteractor(
        defaultClusterMembersListenerInteractor: DefaultClusterMembersListenerInteractor
    ): ClusterMembersListenerInteractor
}