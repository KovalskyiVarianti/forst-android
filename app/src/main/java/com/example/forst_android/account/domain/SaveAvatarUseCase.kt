package com.example.forst_android.account.domain

interface SaveAvatarUseCase {
    suspend fun saveAvatar(byteArray: ByteArray) : SaveAvatarResult
}