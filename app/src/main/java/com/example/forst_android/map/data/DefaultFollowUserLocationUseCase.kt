package com.example.forst_android.map.data

import com.example.forst_android.common.data.database.UserRealtimeDatabase
import com.example.forst_android.map.domain.FollowUserLocationUseCase
import javax.inject.Inject

class DefaultFollowUserLocationUseCase @Inject constructor(
    private val userRealtimeDatabase: UserRealtimeDatabase
) : FollowUserLocationUseCase {
    override fun followUserLocation(
        clusterId: String,
        userId: String,
        followedUserId: String,
        isFollowed: Boolean
    ) {
        userRealtimeDatabase.followUserLocation(clusterId, userId, followedUserId, isFollowed)
    }
}