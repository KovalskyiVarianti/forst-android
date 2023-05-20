package com.example.forst_android.map.domain

import kotlinx.coroutines.flow.Flow

interface PointsListenerInteractor {
    fun addPointsListener(clusterId: String): Flow<List<PointEntity>>
    fun removePointsListener()
}