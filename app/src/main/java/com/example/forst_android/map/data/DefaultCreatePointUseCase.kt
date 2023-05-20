package com.example.forst_android.map.data

import com.example.forst_android.map.domain.CreatePointUseCase
import java.util.UUID
import javax.inject.Inject

class DefaultCreatePointUseCase @Inject constructor(
    private val pointRealtimeDatabase: PointRealtimeDatabase,
) : CreatePointUseCase {

    override fun createPoint(
        clusterId: String,
        userId: String,
        pointName: String,
        lat: Double,
        lng: Double
    ) {
        val currentTime = System.currentTimeMillis()
        val pointId = UUID.randomUUID().toString()
        val point = PointRealtimeEntity(pointId, pointName, lat, lng, userId, currentTime)
        pointRealtimeDatabase.createPoint(clusterId, pointId, point)
    }
}