package com.example.forst_android.account.domain

sealed interface SaveAvatarResult {
    object Success : SaveAvatarResult
    object Fail : SaveAvatarResult
}