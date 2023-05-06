package com.example.forst_android.chats.priv.create.domain

import com.example.forst_android.common.domain.entity.UserEntity
import kotlinx.coroutines.flow.Flow

interface ClusterMembersListenerInteractor {
    fun addClusterMembersListener(): Flow<List<UserEntity>>
    suspend fun removeClusterMembersListener()
}