package com.example.forst_android.common.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.forst_android.clusters.data.ClusterDao
import com.example.forst_android.clusters.data.ClusterDatabaseEntity

@Database(
    entities = [
        ClusterDatabaseEntity::class
    ],
    exportSchema = false,
    version = 1
)
abstract class ClusterDatabase : RoomDatabase() {
    abstract fun clustersDao() : ClusterDao
}

