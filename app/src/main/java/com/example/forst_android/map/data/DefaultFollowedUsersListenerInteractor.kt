package com.example.forst_android.map.data

import com.example.forst_android.common.data.database.UserRealtimeDatabase
import com.example.forst_android.common.domain.service.UserService
import com.example.forst_android.map.domain.FollowedUserEntity
import com.example.forst_android.map.domain.FollowedUserLocation
import com.example.forst_android.map.domain.FollowedUsersListenerInteractor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DefaultFollowedUsersListenerInteractor @Inject constructor(
    private val userLocationRealtimeDatabase: UserLocationRealtimeDatabase,
    private val userRealtimeDatabase: UserRealtimeDatabase,
) : FollowedUsersListenerInteractor {

    override fun addFollowedUsersListener(
        clusterId: String,
        userId: String
    ): Flow<List<FollowedUserEntity>> {
        return userRealtimeDatabase.addFollowedUserIdsListener(
            clusterId,
            userId
        ).flatMapMerge { ids ->
            userLocationRealtimeDatabase.addUsersLocationsListener(clusterId, ids)
        }.map { userLocations ->
            userLocations.mapNotNull { userLocation ->
                if (userLocation.enabled != true) return@mapNotNull null
                val followedUser = userRealtimeDatabase.getUserById(userLocation.id.orEmpty())

                val followedUserLocation =
                    if (userLocation.lat != null && userLocation.lng != null) {
                        FollowedUserLocation(userLocation.lat, userLocation.lng)
                    } else {
                        null
                    }

                FollowedUserEntity(
                    userLocation.id.orEmpty(),
                    followedUser?.name ?: followedUser?.phoneNumber.orEmpty(),
                    UserService.getPhotoUrl(userLocation.id.orEmpty()),
                    followedUserLocation,
                    userLocation.lastUpdate!!
                )
            }
        }
    }

    override fun removeFollowedUsersListener() {
        userRealtimeDatabase.removeFollowedUserIdsListener()
        userLocationRealtimeDatabase.removeUsersLocationsListener()
    }
}