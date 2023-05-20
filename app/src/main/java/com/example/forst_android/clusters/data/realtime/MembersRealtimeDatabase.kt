package com.example.forst_android.clusters.data.realtime

import com.example.forst_android.common.coroutines.CoroutineDispatchers
import com.example.forst_android.common.data.database.RealtimeRoot
import com.example.forst_android.common.data.database.getReference
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
import kotlin.coroutines.resume

class MembersRealtimeDatabase @Inject constructor(
    realtimeDatabase: FirebaseDatabase,
    coroutineDispatchers: CoroutineDispatchers
) {

    private val job = Job()
    private val coroutineScope = CoroutineScope(coroutineDispatchers.io + job)

    private val membersDatabase = realtimeDatabase.getReference(RealtimeRoot.MEMBERS)

    private val membersIds = MutableSharedFlow<List<String>>(
        1,
        0,
        BufferOverflow.DROP_OLDEST
    )

    private var membersIdsListener: ValueEventListener? = null

    private var lastClusterId: String? = null

    fun joinCluster(userId: String, clusterId: String) {
        membersDatabase.child(clusterId).child(userId).setValue(true)
    }

    fun addClusterMembersIdsListener(clusterId: String): SharedFlow<List<String>> {
        removeMembersIdsListener()
        lastClusterId = clusterId
        membersIdsListener = membersDatabase
            .child(clusterId)
            .addValueEventListener(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.children.mapNotNull { child -> child.key }.let {
                            coroutineScope.launch { membersIds.emit(it) }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Timber.d("New data ${error.message}")
                    }
                }
            )
        return membersIds.asSharedFlow()
    }

    fun removeMembersIdsListener() {
        membersIdsListener?.let {
            lastClusterId?.let { clusterId ->
                membersDatabase.child(clusterId).removeEventListener(it)
            }
        }
        membersIdsListener = null
    }

    suspend fun getMembersIds(clusterId: String) = suspendCancellableCoroutine {
        membersDatabase.child(clusterId).addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    it.resume(snapshot.children.mapNotNull { child -> child.key })
                }

                override fun onCancelled(error: DatabaseError) {
                    Timber.d("New data ${error.message}")
                }
            }
        )
    }
}