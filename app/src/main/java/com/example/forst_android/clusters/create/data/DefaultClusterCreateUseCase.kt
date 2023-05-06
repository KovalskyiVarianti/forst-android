package com.example.forst_android.clusters.create.data

import com.example.forst_android.clusters.data.ClusterPreferences
import com.example.forst_android.clusters.data.realtime.ClusterRealtimeDatabase
import com.example.forst_android.clusters.data.realtime.ClusterRealtimeEntity
import com.example.forst_android.clusters.create.domain.ClusterCreateUseCase
import com.example.forst_android.clusters.data.realtime.MembersRealtimeDatabase
import com.example.forst_android.common.data.database.UserRealtimeDatabase
import com.example.forst_android.common.domain.service.UserService
import java.util.*
import javax.inject.Inject

class DefaultClusterCreateUseCase @Inject constructor(
    private val userService: UserService,
    private val clusterRealtimeDatabase: ClusterRealtimeDatabase,
    private val membersRealtimeDatabase: MembersRealtimeDatabase,
    private val userRealtimeDatabase: UserRealtimeDatabase,
    private val clusterPreferences: ClusterPreferences,
) : ClusterCreateUseCase {
    override suspend fun createCluster(name: String, isPrivate: Boolean) {
        val clusterId = UUID.randomUUID().toString()
        val userId = userService.userUID.orEmpty()
        clusterRealtimeDatabase.createCluster(
            ClusterRealtimeEntity(
                id = clusterId,
                name = name,
                ownerId = userId,
                private = isPrivate,
            )
        )
        membersRealtimeDatabase.joinCluster(userId, clusterId)
        userRealtimeDatabase.joinCluster(userId, clusterId)
        clusterPreferences.selectCluster(clusterId)
    }
}