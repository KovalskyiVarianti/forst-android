package com.example.forst_android.map.domain

import kotlinx.coroutines.flow.Flow

interface FollowedUsersListenerInteractor {
    fun addFollowedUsersListener(clusterId: String, userId: String) : Flow<List<FollowedUserEntity>>
    fun removeFollowedUsersListener(clusterId: String, userId: String)
}