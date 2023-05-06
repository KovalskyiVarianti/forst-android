package com.example.forst_android.account.domain

sealed interface ChangeNameResult {
    object Success: ChangeNameResult
    object Failure: ChangeNameResult
}