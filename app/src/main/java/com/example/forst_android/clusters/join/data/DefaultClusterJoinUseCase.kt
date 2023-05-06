package com.example.forst_android.clusters.join.data

import com.example.forst_android.clusters.data.ClusterPreferences
import com.example.forst_android.clusters.data.realtime.MembersRealtimeDatabase
import com.example.forst_android.clusters.join.domain.ClusterJoinUseCase
import com.example.forst_android.common.data.database.UserRealtimeDatabase
import com.example.forst_android.common.domain.service.UserService
import javax.inject.Inject

class DefaultClusterJoinUseCase @Inject constructor(
    private val userService: UserService,
    private val membersRealtimeDatabase: MembersRealtimeDatabase,
    private val userRealtimeDatabase: UserRealtimeDatabase,
    private val clusterPreferences: ClusterPreferences,
) : ClusterJoinUseCase {
    override suspend fun joinCluster(id: String) {
        membersRealtimeDatabase.joinCluster(userService.userUID.orEmpty(), id)
        userRealtimeDatabase.joinCluster(userService.userUID.orEmpty(), id)
        clusterPreferences.selectCluster(id)
    }
}