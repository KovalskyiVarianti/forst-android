package com.example.forst_android.clusters.join.ui

import com.example.forst_android.clusters.domain.ClusterEntity
import com.example.forst_android.clusters.join.ui.adapter.ClusterJoinItem
import com.example.forst_android.common.domain.service.UserService
import javax.inject.Inject

class ClusterJoinItemsMapper @Inject constructor(
    private val userService: UserService,
) {
    fun map(clusterEntities: List<ClusterEntity>): List<ClusterJoinItem> {
        val clustersByType = clusterEntities.groupBy { it.getClusterType() }.toSortedMap()
        return clustersByType.flatMap { (type, clusters) ->
            clusters.sortedBy { it.name }.map { cluster ->
                ClusterJoinItem(cluster.id, cluster.name, type)
            }
        }
    }

    private fun ClusterEntity.getClusterType() = when (userService.userUID) {
        ownerId -> ClusterJoinItem.ClusterJoinType.SELF
        in membersIds -> ClusterJoinItem.ClusterJoinType.JOINED
        else -> ClusterJoinItem.ClusterJoinType.NOT_JOINED
    }
}