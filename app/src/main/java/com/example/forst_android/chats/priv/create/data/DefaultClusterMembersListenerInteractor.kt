package com.example.forst_android.chats.priv.create.data

import com.example.forst_android.chats.priv.create.domain.ClusterMembersListenerInteractor
import com.example.forst_android.clusters.data.ClusterPreferences
import com.example.forst_android.clusters.data.realtime.MembersRealtimeDatabase
import com.example.forst_android.common.data.database.UserRealtimeDatabase
import com.example.forst_android.common.domain.entity.UserEntity
import com.example.forst_android.common.domain.service.UserService
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class DefaultClusterMembersListenerInteractor @Inject constructor(
    private val userRealtimeDatabase: UserRealtimeDatabase,
    private val membersRealtimeDatabase: MembersRealtimeDatabase,
    private val clusterPreferences: ClusterPreferences,
) : ClusterMembersListenerInteractor {
    override fun addClusterMembersListener(): Flow<List<UserEntity>> {
        return clusterPreferences.getSelectedClusterId().flatMapMerge {
            membersRealtimeDatabase.addClusterMembersIdsListener(
                it ?: throw IllegalArgumentException()
            )
        }.flatMapMerge { membersIds ->
            userRealtimeDatabase.addUsersByIdsListener(membersIds)
        }.map { users ->
            users.map { user ->
                val userId = user.id.orEmpty()
                UserEntity(
                    id = userId,
                    name = user.name.orEmpty(),
                    phoneNumber = user.phoneNumber.orEmpty(),
                    photoUri = UserService.getPhotoUrl(userId)
                )
            }
        }
    }

    override suspend fun removeClusterMembersListener() {
        membersRealtimeDatabase.removeMembersIdsListener()
        userRealtimeDatabase.removeUsersByIdsListener()
    }
}