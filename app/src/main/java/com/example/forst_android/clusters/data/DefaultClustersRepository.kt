package com.example.forst_android.clusters.data

import com.example.forst_android.clusters.domain.ClusterEntity
import com.example.forst_android.clusters.domain.ClustersRepository
import com.example.forst_android.common.data.database.ClusterDao
import com.example.forst_android.common.data.database.ClusterDatabaseEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.*
import javax.inject.Inject

class DefaultClustersRepository @Inject constructor(
    private val clusterDao: ClusterDao
) : ClustersRepository {

    suspend fun selectCluster(id: String) {
        clusterDao.selectCluster(id)
    }

    override fun getClusters(): Flow<List<ClusterEntity>> {
        return clusterDao.getAllClusters().map { clusters ->
            clusters.map { cluster ->
                ClusterEntity(cluster.id, cluster.name, cluster.isSelected)
            }
        }
    }

    override suspend fun createCluster(name: String) {
        clusterDao.createAndSelect(
            ClusterDatabaseEntity(
                UUID.randomUUID().toString(),
                name,
                isSelected = true
            )
        )
    }

    override suspend fun isEmpty() = clusterDao.isEmpty()
}