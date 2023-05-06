package com.example.forst_android.main.data

import com.example.forst_android.common.data.database.UserRealtimeDatabase
import com.example.forst_android.common.domain.service.UserService
import com.example.forst_android.main.domain.IsJoinedClustersExistUseCase
import javax.inject.Inject

class DefaultIsJoinedClustersExistUseCase @Inject constructor(
    private val userService: UserService,
    private val userRealtimeDatabase: UserRealtimeDatabase
) : IsJoinedClustersExistUseCase {
    override suspend fun isJoinedClustersExist(): Boolean {
        return userRealtimeDatabase.haveJoinedClusters(userService.userUID.orEmpty())
    }
}