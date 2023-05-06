package com.example.forst_android.account.domain

interface ChangeUserNameUseCase {
    suspend fun changeName(name: String) : ChangeNameResult
}