package com.example.forst_android.clusters.join.domain

interface ClusterJoinUseCase {
    suspend fun joinCluster(id: String)
}