package com.example.forst_android.clusters.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ClusterDao {



    @Query("SELECT * FROM clusters")
    fun getAllClusters() : Flow<List<ClusterDatabaseEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCluster(cluster: ClusterDatabaseEntity)

    @Delete
    suspend fun removeCluster(cluster: ClusterDatabaseEntity)

    @Query("SELECT * FROM clusters WHERE isLocationShareEnabled = 1")
    fun getShareLocationClusters() : Flow<List<ClusterDatabaseEntity>>
}