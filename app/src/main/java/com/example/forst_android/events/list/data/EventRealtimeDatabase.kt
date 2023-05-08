package com.example.forst_android.events.list.data

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

class EventRealtimeDatabase @Inject constructor(
    realtimeDatabase: FirebaseDatabase,
    coroutineDispatchers: CoroutineDispatchers
) {

    private val job = Job()
    private val coroutineScope = CoroutineScope(coroutineDispatchers.io + job)

    private val eventDatabase = realtimeDatabase.getReference(RealtimeRoot.EVENTS)

    private val events = MutableSharedFlow<List<EventRealtimeEntity>>(
        1,
        0,
        BufferOverflow.DROP_OLDEST
    )

    private var eventListener: ValueEventListener? = null

    fun addEventListener(clusterId: String): SharedFlow<List<EventRealtimeEntity>> {
        removeEventListener(clusterId)
        eventListener = eventDatabase.child(clusterId).addValueEventListener(
            DataEventListener { snapshot: DataSnapshot ->
                snapshot.children.mapNotNull { child ->
                    child.getValue(EventRealtimeEntity::class.java)
                }.let {
                    coroutineScope.launch { events.emit(it) }
                }
            }
        )
        return events.asSharedFlow()
    }

    fun removeEventListener(clusterId: String) {
        eventListener?.let { eventDatabase.child(clusterId).removeEventListener(it) }
        eventListener = null
    }

    fun createEvent(clusterId: String, event: EventRealtimeEntity) {
        eventDatabase.child(clusterId).child(event.id.orEmpty()).setValue(event)
    }
}