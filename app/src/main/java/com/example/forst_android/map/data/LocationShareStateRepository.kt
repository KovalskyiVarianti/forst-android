package com.example.forst_android.map.data

import com.example.forst_android.clusters.data.ClusterDao
import com.example.forst_android.clusters.data.ClusterDatabaseEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocationShareStateRepository @Inject constructor(
    private val clusterDao: ClusterDao,
) {

    suspend fun changeLocationShareStateForCluster(clusterId: String) {
        val locationState = isLocationShareEnabled(clusterId).first()
        clusterDao.addCluster(ClusterDatabaseEntity(clusterId, locationState.not()))
    }

    fun isLocationShareEnabled(clusterId: String): Flow<Boolean> {
        return clusterDao.getShareLocationClusters().map { clustersStates ->
            clustersStates.find { it.id == clusterId }?.isLocationShareEnabled ?: false
        }
    }

    suspend fun getAllClusterShareStates(): List<ClusterDatabaseEntity> {
        return clusterDao.getAllClusters().first()
    }
}