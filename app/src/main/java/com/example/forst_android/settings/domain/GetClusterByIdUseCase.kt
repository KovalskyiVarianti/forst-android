package com.example.forst_android.settings.domain

import com.example.forst_android.clusters.domain.ClusterEntity

interface GetClusterByIdUseCase {
    suspend fun getCluster(id: String) : ClusterEntity
}