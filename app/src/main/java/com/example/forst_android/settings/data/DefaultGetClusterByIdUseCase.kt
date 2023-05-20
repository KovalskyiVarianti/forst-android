package com.example.forst_android.settings.data

import com.example.forst_android.clusters.data.realtime.ClusterRealtimeDatabase
import com.example.forst_android.clusters.data.realtime.MembersRealtimeDatabase
import com.example.forst_android.clusters.domain.ClusterEntity
import com.example.forst_android.settings.domain.GetClusterByIdUseCase
import javax.inject.Inject

class DefaultGetClusterByIdUseCase @Inject constructor(
    private val clusterRealtimeDatabase: ClusterRealtimeDatabase,
    private val membersRealtimeDatabase: MembersRealtimeDatabase,
) : GetClusterByIdUseCase {
    override suspend fun getCluster(id: String): ClusterEntity {
        return clusterRealtimeDatabase.getClusterById(id).let { clusterRealtimeEntity ->
            val members = membersRealtimeDatabase.getMembersIds(id)
            ClusterEntity(
                clusterRealtimeEntity?.id.orEmpty(),
                members,
                clusterRealtimeEntity?.name.orEmpty(),
                clusterRealtimeEntity?.ownerId.orEmpty(),
                clusterRealtimeEntity?.private ?: false
            )
        }
    }
}