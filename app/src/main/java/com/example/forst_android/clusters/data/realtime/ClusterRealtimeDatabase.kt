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
import timber.log.Timber
import javax.inject.Inject

class ClusterRealtimeDatabase @Inject constructor(
    realtimeDatabase: FirebaseDatabase,
    coroutineDispatchers: CoroutineDispatchers
) {

    private val job = Job()
    private val coroutineScope = CoroutineScope(coroutineDispatchers.io + job)

    private val clusterDatabase = realtimeDatabase.getReference(RealtimeRoot.CLUSTERS)

    private val joinedClusters = MutableSharedFlow<List<ClusterRealtimeEntity>>(
        1,
        0,
        BufferOverflow.DROP_OLDEST
    )

    private val allClusters = MutableSharedFlow<List<ClusterRealtimeEntity>>(
        1,
        0,
        BufferOverflow.DROP_OLDEST
    )

    private var allClustersListener: ValueEventListener? = null
    private var joinedClustersListener: ValueEventListener? = null

    fun createCluster(cluster: ClusterRealtimeEntity) {
        clusterDatabase.child(cluster.id.orEmpty()).setValue(cluster)
    }

    fun addJoinedClustersListener(ids: List<String>): SharedFlow<List<ClusterRealtimeEntity>> {
        removeJoinedClustersListener()
        joinedClustersListener = clusterDatabase.addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children
                        .mapNotNull { child ->
                            child.getValue(ClusterRealtimeEntity::class.java)
                                .takeIf { it?.id in ids }
                        }.let {
                            coroutineScope.launch { joinedClusters.emit(it) }
                        }

                }

                override fun onCancelled(error: DatabaseError) {
                    Timber.d("New data ${error.message}")
                }
            }
        )
        return joinedClusters.asSharedFlow()
    }

    fun removeJoinedClustersListener() {
        joinedClustersListener?.let { clusterDatabase.removeEventListener(it) }
        joinedClustersListener = null
    }

    fun addAllClustersListener(): SharedFlow<List<ClusterRealtimeEntity>> {
        removeAllClustersListener()
        allClustersListener = clusterDatabase.addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.mapNotNull { child ->
                        child.getValue(ClusterRealtimeEntity::class.java)
                            ?.takeIf { it.private == false }
                    }.let {
                        coroutineScope.launch { allClusters.emit(it) }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Timber.d("New data ${error.message}")
                }
            }
        )
        return allClusters.asSharedFlow()
    }

    fun removeAllClustersListener() {
        allClustersListener?.let { clusterDatabase.removeEventListener(it) }
        allClustersListener = null
    }


}