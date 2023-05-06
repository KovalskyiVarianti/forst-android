package com.example.forst_android.main.domain

interface IsJoinedClustersExistUseCase {
    suspend fun isJoinedClustersExist() : Boolean
}