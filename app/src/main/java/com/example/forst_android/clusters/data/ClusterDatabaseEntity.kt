package com.example.forst_android.clusters.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "clusters")
data class ClusterDatabaseEntity(
    @PrimaryKey val id: String,
    val isLocationShareEnabled: Boolean
)