package com.example.forst_android.clusters.domain

data class ClusterEntity(
    val id: String,
    val membersIds: List<String>,
    val name: String,
    val ownerId: String,
)
