package com.example.forst_android.clusters.join.data

import com.example.forst_android.clusters.data.realtime.ClusterRealtimeDatabase
import com.example.forst_android.clusters.data.realtime.MembersRealtimeDatabase
import com.example.forst_android.clusters.domain.ClusterEntity
import com.example.forst_android.clusters.join.domain.ClusterAllListenerInteractor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DefaultClusterAllListenerInteractor @Inject constructor(
    private val clusterRealtimeDatabase: ClusterRealtimeDatabase,
    private val membersRealtimeDatabase: MembersRealtimeDatabase,
) : ClusterAllListenerInteractor {
    override fun addAllClustersListener(): Flow<List<ClusterEntity>> {
        return clusterRealtimeDatabase.addAllClustersListener().map { clusters ->
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

    override fun removeAllClustersListener() {
        clusterRealtimeDatabase.removeAllClustersListener()
    }
}