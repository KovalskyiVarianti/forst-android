package com.example.forst_android.main.di

import com.example.forst_android.main.data.DefaultIsJoinedClustersExistUseCase
import com.example.forst_android.main.domain.IsJoinedClustersExistUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface UseCaseModule {

    @Binds
    fun isJoinedClustersExistUseCase(
        defaultIsJoinedClustersExistUseCase: DefaultIsJoinedClustersExistUseCase
    ): IsJoinedClustersExistUseCase
}