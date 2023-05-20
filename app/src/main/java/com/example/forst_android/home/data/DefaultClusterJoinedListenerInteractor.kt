package com.example.forst_android.home.data

import com.example.forst_android.clusters.data.realtime.ClusterRealtimeDatabase
import com.example.forst_android.clusters.data.realtime.MembersRealtimeDatabase
import com.example.forst_android.clusters.domain.ClusterEntity
import com.example.forst_android.common.data.database.UserRealtimeDatabase
import com.example.forst_android.common.domain.service.UserService
import com.example.forst_android.home.domain.ClusterJoinedListenerInteractor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DefaultClusterJoinedListenerInteractor @Inject constructor(
    private val userService: UserService,
    private val userRealtimeDatabase: UserRealtimeDatabase,
    private val clusterRealtimeDatabase: ClusterRealtimeDatabase,
    private val membersRealtimeDatabase: MembersRealtimeDatabase,
) : ClusterJoinedListenerInteractor {
    override fun addJoinedClustersListener(): Flow<List<ClusterEntity>> {
        return userRealtimeDatabase.addJoinedClustersIdsListener(userService.userUID.orEmpty())
            .flatMapMerge {
                clusterRealtimeDatabase.addJoinedClustersListener(it)
            }.map { clusters ->
                clusters.map { cluster ->
                    ClusterEntity(
                        cluster.id.orEmpty(),
                        membersRealtimeDatabase.getMembersIds(cluster.id.orEmpty()),
                        cluster.name.orEmpty(),
                        cluster.ownerId.orEmpty(),
                        cluster.private ?: false
                    )
                }
            }
    }

    override fun removeJoinedClustersListener() {
        userRealtimeDatabase.removeJoinedClustersIdsListener()
        clusterRealtimeDatabase.removeJoinedClustersListener()
    }
}