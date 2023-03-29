package com.example.forst_android.auth.di

import com.example.forst_android.auth.data.service.FirebaseAuthService
import com.example.forst_android.auth.domain.service.AuthService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
interface ActivityServiceModule {

    @Binds
    @ActivityScoped
    fun authService(firebaseAuthService: FirebaseAuthService): AuthService
}