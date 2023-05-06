package com.example.forst_android.clusters.join.di

import com.example.forst_android.clusters.join.data.DefaultClusterJoinUseCase
import com.example.forst_android.clusters.join.domain.ClusterJoinUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface UseCaseModule {

    @Binds
    fun clusterJoinUseCase(
        defaultClusterJoinUseCase: DefaultClusterJoinUseCase
    ): ClusterJoinUseCase
}