package com.example.forst_android.map.data

import com.example.forst_android.clusters.data.realtime.MembersRealtimeDatabase
import com.example.forst_android.common.data.database.UserRealtimeDatabase
import com.example.forst_android.common.domain.service.UserService
import com.example.forst_android.map.domain.ClusterMapUserListenerInteractor
import com.example.forst_android.map.domain.MapUserEntity
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class DefaultClusterMapUserListenerInteractor @Inject constructor(
    private val membersRealtimeDatabase: MembersRealtimeDatabase,
    private val userRealtimeDatabase: UserRealtimeDatabase,
    private val locationRealtimeDatabase: UserLocationRealtimeDatabase,
) : ClusterMapUserListenerInteractor {

    override fun addClusterMapUserListener(
        clusterId: String,
        userId: String
    ): Flow<List<MapUserEntity>> {
        return membersRealtimeDatabase.addClusterMembersIdsListener(clusterId).flatMapMerge { ids ->
            userRealtimeDatabase.addUsersByIdsListener(ids).combine(
                locationRealtimeDatabase.addUsersLocationsListener(clusterId, ids)
            ) { users, locations ->
                users.mapNotNull { user ->
                    if (user.id == userId) return@mapNotNull null
                    val followedUserId = user.id.orEmpty()
                    MapUserEntity(
                        followedUserId,
                        user.name ?: user.phoneNumber.orEmpty(),
                        UserService.getPhotoUrl(followedUserId),
                        userRealtimeDatabase.isUserFollowed(clusterId, userId, followedUserId),
                        locations.find { it.id == followedUserId }?.enabled ?: false
                    )
                }
            }
        }
    }

    override fun removeClusterMapUserListener() {
        membersRealtimeDatabase.removeMembersIdsListener()
        userRealtimeDatabase.removeUsersByIdsListener()
    }
}