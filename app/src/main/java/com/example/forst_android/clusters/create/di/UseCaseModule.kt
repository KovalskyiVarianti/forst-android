package com.example.forst_android.clusters.create.di

import com.example.forst_android.clusters.create.data.DefaultClusterCreateUseCase
import com.example.forst_android.clusters.create.domain.ClusterCreateUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface UseCaseModule {

    @Binds
    fun clusterCreateUseCase(
        defaultClusterCreateUseCase: DefaultClusterCreateUseCase
    ): ClusterCreateUseCase
}