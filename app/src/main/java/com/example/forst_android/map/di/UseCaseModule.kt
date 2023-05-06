package com.example.forst_android.map.di

import com.example.forst_android.map.data.DefaultFollowUserLocationUseCase
import com.example.forst_android.map.domain.FollowUserLocationUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface UseCaseModule {
    @Binds
    fun followUserLocationUseCase(
        defaultFollowUserLocationUseCase: DefaultFollowUserLocationUseCase
    ): FollowUserLocationUseCase
}