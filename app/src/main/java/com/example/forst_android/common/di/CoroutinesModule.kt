package com.example.forst_android.common.di

import com.example.forst_android.common.coroutines.CoroutineDispatchers
import com.example.forst_android.common.coroutines.DefaultCoroutineDispatchers
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface CoroutinesModule {

    @Binds
    fun coroutineDispatchers(defaultCoroutineDispatchers: DefaultCoroutineDispatchers): CoroutineDispatchers
}