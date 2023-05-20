package com.example.forst_android.settings.di

import com.example.forst_android.settings.data.DefaultGetClusterByIdUseCase
import com.example.forst_android.settings.data.DefaultUpdateClusterPrivacyUseCase
import com.example.forst_android.settings.domain.GetClusterByIdUseCase
import com.example.forst_android.settings.domain.UpdateClusterPrivacyUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface UseCaseModule {

    @Binds
    fun getClusterByIdUseCase(
        defaultGetClusterByIdUseCase: DefaultGetClusterByIdUseCase
    ): GetClusterByIdUseCase

    @Binds
    fun updateClusterPrivacyUseCase(
        defaultUpdateClusterPrivacyUseCase: DefaultUpdateClusterPrivacyUseCase
    ): UpdateClusterPrivacyUseCase
}