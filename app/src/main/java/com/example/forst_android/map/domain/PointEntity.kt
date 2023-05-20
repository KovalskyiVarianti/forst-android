package com.example.forst_android.map.domain

data class PointEntity(
    val id: String,
    val name: String,
    val lat: Double,
    val lng: Double,
    val creatorName: String,
    val createTime: Long,
)
