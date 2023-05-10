package com.example.forst_android.chats.group.create.data

import com.example.forst_android.common.coroutines.CoroutineDispatchers
import com.example.forst_android.common.data.database.DataEventListener
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

class ChatGroupRealtimeDatabase @Inject constructor(
    realtimeDatabase: FirebaseDatabase,
    coroutineDispatchers: CoroutineDispatchers
) {

    private val groupDatabase = realtimeDatabase.getReference(RealtimeRoot.GROUPS)

    private val job = Job()
    private val coroutineScope = CoroutineScope(coroutineDispatchers.io + job)

    private val chatsGroup = MutableSharedFlow<List<ChatGroupRealtimeEntity>>(
        1,
        0,
        BufferOverflow.DROP_OLDEST
    )

    private var chatGroupEventListener: ValueEventListener? = null

    fun createGroupChat(
        clusterId: String,
        groupId: String,
        groupName: String,
        selfId: String,
        membersIds: List<String>
    ) {
        val allMembers = (listOf(selfId) + membersIds)
        allMembers.forEach { memberId ->
            createGroupForMember(
                clusterId,
                groupId,
                groupName,
                memberId,
                allMembers.associateWith { true })
        }
    }

    fun addChatGroupListener(
        clusterId: String,
        userId: String
    ): SharedFlow<List<ChatGroupRealtimeEntity>> {
        removeChatGroupListener(clusterId, userId)
        chatGroupEventListener = groupDatabase
            .child(clusterId)
            .child(userId)
            .addValueEventListener(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.children.mapNotNull { child ->
                            child.getValue(
                                ChatGroupRealtimeEntity::class.java
                            )
                        }.let { coroutineScope.launch { chatsGroup.emit(it) } }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Timber.d("New data ${error.message}")
                    }

                }
            )
        return chatsGroup.asSharedFlow()
    }

    fun removeChatGroupListener(clusterId: String, userId: String) {
        chatGroupEventListener?.let {
            groupDatabase.child(clusterId).child(userId).removeEventListener(it)
        }
        chatGroupEventListener = null
    }

    suspend fun getGroupById(clusterId: String, userId: String, groupId: String) =
        suspendCancellableCoroutine {
            groupDatabase
                .child(clusterId)
                .child(userId)
                .child(groupId)
                .addListenerForSingleValueEvent(
                    DataEventListener { snapshot ->
                        it.resume(snapshot.getValue(ChatGroupRealtimeEntity::class.java))
                    }
                )
        }

    private fun createGroupForMember(
        clusterId: String,
        groupId: String,
        groupName: String,
        userId: String,
        members: Map<String, Boolean>
    ) {
        groupDatabase
            .child(clusterId)
            .child(userId)
            .child(groupId)
            .setValue(
                ChatGroupRealtimeEntity(id = groupId, name = groupName, members = members)
            )
    }
}