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

class PointRealtimeDatabase @Inject constructor(
    private val realtimeDatabase: FirebaseDatabase,
    private val coroutineDispatchers: CoroutineDispatchers,
) {

    private val job = Job()
    private val coroutineScope = CoroutineScope(coroutineDispatchers.io + job)

    private val pointDatabase = realtimeDatabase.getReference(RealtimeRoot.POINTS)

    private val points = MutableSharedFlow<List<PointRealtimeEntity>>(
        1,
        0,
        BufferOverflow.DROP_OLDEST
    )

    private var pointsListener: ValueEventListener? = null

    private var lastClusterId: String? = null

    fun createPoint(clusterId: String, pointId: String, point: PointRealtimeEntity) {
        pointDatabase
            .child(clusterId)
            .child(pointId)
            .setValue(point)
    }

    fun addPointsListener(
        clusterId: String,
    ): SharedFlow<List<PointRealtimeEntity>> {
        removePointsListener()
        lastClusterId = clusterId
        pointsListener = pointDatabase.child(clusterId).addValueEventListener(
            DataEventListener { snapshot: DataSnapshot ->
                snapshot.children.mapNotNull { child ->
                    child.getValue(PointRealtimeEntity::class.java)
                }.let {
                    coroutineScope.launch { points.emit(it) }
                }
            }
        )
        return points.asSharedFlow()
    }

    fun removePointsListener() {
        pointsListener?.let {
            lastClusterId?.let { clusterId ->
                pointDatabase.child(clusterId).removeEventListener(it)
            }
        }
        pointsListener = null
    }
}