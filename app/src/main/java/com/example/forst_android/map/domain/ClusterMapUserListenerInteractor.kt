package com.example.forst_android.map.domain

import kotlinx.coroutines.flow.Flow

interface ClusterMapUserListenerInteractor {
    fun addClusterMapUserListener(clusterId: String, userId: String): Flow<List<MapUserEntity>>
    fun removeClusterMapUserListener(clusterId: String, userId: String)
}