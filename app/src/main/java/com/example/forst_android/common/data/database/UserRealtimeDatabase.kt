package com.example.forst_android.common.data.database

import com.example.forst_android.common.coroutines.CoroutineDispatchers
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

@Singleton
class UserRealtimeDatabase @Inject constructor(
    realtimeDatabase: FirebaseDatabase,
    coroutineDispatchers: CoroutineDispatchers,
) {

    private val job = Job()
    private val coroutineScope = CoroutineScope(coroutineDispatchers.io + job)

    private val userDatabase = realtimeDatabase.getReference(RealtimeRoot.USERS)

    private val joinedClustersIds = MutableSharedFlow<List<String>>(
        1,
        0,
        BufferOverflow.DROP_OLDEST
    )

    private val usersByIds = MutableSharedFlow<List<UserRealtimeEntity>>(
        1,
        0,
        BufferOverflow.DROP_OLDEST
    )

    private val followedUsersIds = MutableSharedFlow<List<String>>(
        1,
        0,
        BufferOverflow.DROP_OLDEST
    )

    private var joinedClustersIdsListener: ValueEventListener? = null
    private var usersByIdsListener: ValueEventListener? = null
    private var followedUsersListener: ValueEventListener? = null

    fun addJoinedClustersIdsListener(userId: String): SharedFlow<List<String>> {
        userDatabase
            .child(userId)
            .child(RealtimeRoot.CLUSTERS)
            .apply {
                joinedClustersIdsListener?.let { removeEventListener(it) }
                joinedClustersIdsListener = addValueEventListener(
                    object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            snapshot.children
                                .mapNotNull { child -> child.key }
                                .let { ids -> coroutineScope.launch { joinedClustersIds.emit(ids) } }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Timber.d("New data ${error.message}")
                        }
                    }
                )
            }
        return joinedClustersIds.asSharedFlow()
    }

    fun removeJoinedClustersIdsListener(userId: String) {
        joinedClustersIdsListener?.let {
            userDatabase
                .child(userId)
                .child(RealtimeRoot.CLUSTERS)
                .removeEventListener(it)
        }
        joinedClustersIdsListener = null
    }

    fun createUser(user: UserRealtimeEntity) {
        coroutineScope.launch {
            if (getUserById(user.id.orEmpty()) == null) {
                userDatabase.child(user.id.orEmpty()).setValue(user)
            }
        }
    }

    fun joinCluster(userId: String, clusterId: String) = userDatabase
        .child(userId)
        .child(RealtimeRoot.CLUSTERS)
        .child(clusterId)
        .setValue(true)

    suspend fun haveJoinedClusters(userId: String) = suspendCancellableCoroutine {
        userDatabase.child(userId).child(RealtimeRoot.CLUSTERS).addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    it.resume(snapshot.hasChildren())
                }

                override fun onCancelled(error: DatabaseError) {
                    Timber.d("New data ${error.message}")
                    it.resume(false)
                }
            }
        )
    }

    fun addUsersByIdsListener(ids: List<String>): SharedFlow<List<UserRealtimeEntity>> {
        removeUsersByIdsListener()
        usersByIdsListener = userDatabase.addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.mapNotNull { child ->
                        child.getValue(UserRealtimeEntity::class.java).takeIf { it?.id in ids }
                    }.let {
                        coroutineScope.launch { usersByIds.emit(it) }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Timber.d("New data ${error.message}")
                }

            }
        )
        return usersByIds.asSharedFlow()
    }

    fun removeUsersByIdsListener() {
        usersByIdsListener?.let { userDatabase.removeEventListener(it) }
        usersByIdsListener = null
    }

    suspend fun getUserById(id: String) = suspendCancellableCoroutine {
        userDatabase.child(id).addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    it.resume(snapshot.getValue(UserRealtimeEntity::class.java))
                }

                override fun onCancelled(error: DatabaseError) {
                    Timber.d("New data ${error.message}")
                    it.resume(null)
                }
            }
        )
    }

    fun addFollowedUserIdsListener(clusterId: String, userId: String): SharedFlow<List<String>> {
        userDatabase
            .child(userId)
            .child(RealtimeRoot.LOCATIONS)
            .child(clusterId)
            .apply {
                removeFollowedUserIdsListener(clusterId, userId)
                followedUsersListener = addValueEventListener(
                    object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            snapshot.children
                                .mapNotNull { child -> child.key.takeIf { child.getValue(Boolean::class.java) == true } }
                                .let { ids -> coroutineScope.launch { followedUsersIds.emit(ids) } }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Timber.d("New data ${error.message}")
                        }
                    }
                )
            }
        return followedUsersIds.asSharedFlow()
    }

    fun removeFollowedUserIdsListener(clusterId: String, userId: String) {
        followedUsersListener?.let {
            userDatabase
                .child(userId)
                .child(RealtimeRoot.LOCATIONS)
                .child(clusterId)
                .removeEventListener(it)
        }
        followedUsersListener = null
    }

    fun followUserLocation(
        clusterId: String,
        userId: String,
        followedUserId: String,
        isFollowed: Boolean
    ) {
        if (isFollowed) {
            userDatabase
                .child(userId)
                .child(RealtimeRoot.LOCATIONS)
                .child(clusterId)
                .child(followedUserId)
                .setValue(true)
        } else {
            userDatabase
                .child(userId)
                .child(RealtimeRoot.LOCATIONS)
                .child(clusterId)
                .child(followedUserId)
                .removeValue()
        }
    }

    suspend fun isUserFollowed(
        clusterId: String,
        userId: String,
        followedUserId: String
    ) = suspendCancellableCoroutine {
        userDatabase
            .child(userId)
            .child(RealtimeRoot.LOCATIONS)
            .child(clusterId)
            .child(followedUserId)
            .addListenerForSingleValueEvent(
                DataEventListener { snapshot: DataSnapshot ->
                    it.resume(snapshot.getValue(Any::class.java) != null)
                }
            )
    }

    fun updateName(userId: String, userName: String) =
        userDatabase.child(userId).child(RealtimeRoot.NAME).setValue(userName)
}