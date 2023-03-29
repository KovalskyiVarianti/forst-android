package com.example.forst_android.common.di

import com.example.forst_android.common.data.service.FirebaseUserService
import com.example.forst_android.common.domain.service.UserService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface ServiceModule {

    @Binds
    fun userService(firebaseUserService: FirebaseUserService): UserService
}