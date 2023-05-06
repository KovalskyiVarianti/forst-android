package com.example.forst_android.clusters.join.domain

import com.example.forst_android.clusters.domain.ClusterEntity
import kotlinx.coroutines.flow.Flow

interface ClusterAllListenerInteractor {
    fun addAllClustersListener(): Flow<List<ClusterEntity>>
    fun removeAllClustersListener()
}