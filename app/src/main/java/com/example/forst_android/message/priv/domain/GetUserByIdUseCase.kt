package com.example.forst_android.message.priv.domain

import com.example.forst_android.common.domain.entity.UserEntity

interface GetUserByIdUseCase {
    suspend fun getUser(id: String) : UserEntity
}