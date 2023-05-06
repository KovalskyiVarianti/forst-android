package com.example.forst_android.home.domain

import com.example.forst_android.clusters.domain.ClusterEntity
import kotlinx.coroutines.flow.Flow

interface ClusterJoinedListenerInteractor {
    fun addJoinedClustersListener() : Flow<List<ClusterEntity>>
    fun removeJoinedClustersListener()
}