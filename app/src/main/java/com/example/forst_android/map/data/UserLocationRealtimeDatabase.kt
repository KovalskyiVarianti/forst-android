package com.example.forst_android.map.data

import com.example.forst_android.common.coroutines.CoroutineDispatchers
import com.example.forst_android.common.data.database.DataEventListener
import com.example.forst_android.common.data.database.RealtimeRoot
import com.example.forst_android.common.data.database.getReference
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class UserLocationRealtimeDatabase @Inject constructor(
    private val realtimeDatabase: FirebaseDatabase,
    coroutineDispatchers: CoroutineDispatchers,
) {

    private val job = Job()
    private val coroutineScope = CoroutineScope(coroutineDispatchers.io + job)

    private val locationDatabase = realtimeDatabase.getReference(RealtimeRoot.LOCATIONS)

    private val usersLocations = MutableSharedFlow<List<UserLocationRealtimeEntity>>(
        1,
        0,
        BufferOverflow.DROP_OLDEST
    )

    private var usersLocationsListener: ValueEventListener? = null

    private var lastClusterId: String? = null

    fun sendLocation(
        clusterId: String,
        userId: String,
        lat: Double?,
        lng: Double?,
        isEnabled: Boolean,
        updateTime: Long,
    ) {
        locationDatabase.child(clusterId).child(userId).setValue(
            UserLocationRealtimeEntity(userId, lat, lng, isEnabled, updateTime)
        )
    }

    fun addUsersLocationsListener(
        clusterId: String,
        userIds: List<String>
    ): SharedFlow<List<UserLocationRealtimeEntity>> {
        removeUsersLocationsListener()
        lastClusterId = clusterId
        usersLocationsListener = locationDatabase.child(clusterId).addValueEventListener(
            DataEventListener { snapshot: DataSnapshot ->
                snapshot.children.mapNotNull { child ->
                    child.getValue(UserLocationRealtimeEntity::class.java)
                        .takeIf { it?.id in userIds }
                }.let {
                    coroutineScope.launch { usersLocations.emit(it) }
                }
            }
        )
        return usersLocations.asSharedFlow()
    }

    fun removeUsersLocationsListener() {
        usersLocationsListener?.let {
            lastClusterId?.let { clusterId ->
                locationDatabase.child(clusterId).removeEventListener(it)
            }
        }
        usersLocationsListener = null
    }
}