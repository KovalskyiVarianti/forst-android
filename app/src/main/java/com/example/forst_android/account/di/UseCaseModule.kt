package com.example.forst_android.account.di

import com.example.forst_android.account.data.DefaultChangeUserNameUseCase
import com.example.forst_android.account.data.DefaultGetAccountDataUseCase
import com.example.forst_android.account.domain.ChangeUserNameUseCase
import com.example.forst_android.account.domain.GetAccountDataUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface UseCaseModule {

    @Binds
    fun getAccountDataUseCase(defaultGetAccountDataUseCase: DefaultGetAccountDataUseCase): GetAccountDataUseCase

    @Binds
    fun changeUserNameUseCase(defaultChangeUserNameUseCase: DefaultChangeUserNameUseCase): ChangeUserNameUseCase
}