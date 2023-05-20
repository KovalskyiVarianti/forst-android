package com.example.forst_android.map.domain

interface CreatePointUseCase {
    fun createPoint(clusterId: String, userId: String, pointName: String, lat: Double, lng: Double)
}