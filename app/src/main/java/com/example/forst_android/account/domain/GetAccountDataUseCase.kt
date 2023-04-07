package com.example.forst_android.account.domain

interface GetAccountDataUseCase {
    suspend fun getAccountData() : AccountData
}