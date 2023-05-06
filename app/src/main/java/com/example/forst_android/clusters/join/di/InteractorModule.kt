package com.example.forst_android.clusters.join.di

import com.example.forst_android.clusters.join.data.DefaultClusterAllListenerInteractor
import com.example.forst_android.clusters.join.domain.ClusterAllListenerInteractor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface InteractorModule {

    @Binds
    fun clusterAllListenerInteractor(
        defaultClusterAllListenerInteractor: DefaultClusterAllListenerInteractor
    ): ClusterAllListenerInteractor
}