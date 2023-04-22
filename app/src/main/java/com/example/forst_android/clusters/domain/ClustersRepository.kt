package com.example.forst_android.clusters.domain

import kotlinx.coroutines.flow.Flow

interface ClustersRepository {
    fun getClusters(): Flow<List<ClusterEntity>>
    suspend fun createCluster(name: String)
    suspend fun isEmpty(): Boolean
}