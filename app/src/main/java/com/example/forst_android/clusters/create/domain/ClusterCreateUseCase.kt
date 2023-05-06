package com.example.forst_android.clusters.create.domain

interface ClusterCreateUseCase {
    suspend fun createCluster(name: String, isPrivate: Boolean)
}