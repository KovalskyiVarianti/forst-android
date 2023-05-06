package com.example.forst_android.map.domain

data class FollowedUserEntity(
    val id: String,
    val name: String,
    val imageUrl : String,
    val location: FollowedUserLocation?,
    val lastUpdate: Long
)
