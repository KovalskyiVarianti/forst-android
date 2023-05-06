package com.example.forst_android.map.domain

interface FollowUserLocationUseCase {
    fun followUserLocation(
        clusterId: String,
        userId: String,
        followedUserId: String,
        isFollowed: Boolean
    )
}