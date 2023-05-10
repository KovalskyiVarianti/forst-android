package com.example.forst_android.message.group.data

import com.example.forst_android.common.coroutines.CoroutineDispatchers
import com.example.forst_android.common.data.database.DataEventListener
import com.example.forst_android.common.data.database.RealtimeRoot
import com.example.forst_android.common.data.database.child
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

class MessageGroupRealtimeDatabase @Inject constructor(
    private val realtimeDatabase: FirebaseDatabase,
    private val coroutineDispatchers: CoroutineDispatchers,
) {

    private val job = Job()
    private val coroutineScope = CoroutineScope(coroutineDispatchers.io + job)

    private val messageDatabase = realtimeDatabase.getReference(RealtimeRoot.GROUPS_MESSAGES)

    private val groupDatabase = realtimeDatabase.getReference(RealtimeRoot.GROUPS)

    private val messages = MutableSharedFlow<List<MessageGroupRealtimeEntity>>(
        1,
        0,
        BufferOverflow.DROP_OLDEST
    )

    private var messageListener: ValueEventListener? = null

    suspend fun getMessageById(
        clusterId: String,
        userId: String,
        groupId: String,
        messageId: String
    ) = suspendCancellableCoroutine {
        messageDatabase
            .child(clusterId)
            .child(userId)
            .child(groupId)
            .child(messageId)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        it.resume(snapshot.getValue(MessageGroupRealtimeEntity::class.java))
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Timber.d("New data ${error.message}")
                    }
                }
            )
    }

    fun sendMessage(
        clusterId: String,
        groupId: String,
        groupMembers: List<String>,
        message: MessageGroupRealtimeEntity
    ) {
        groupMembers.forEach { memberId ->
            sendMessage(clusterId, groupId, memberId, message)
        }
    }

    fun addMessageListener(
        clusterId: String,
        userId: String,
        groupId: String
    ): SharedFlow<List<MessageGroupRealtimeEntity>> {
        removeMessageListener(clusterId, userId, groupId)
        messageListener = messageDatabase
            .child(clusterId)
            .child(userId)
            .child(groupId)
            .addValueEventListener(
                DataEventListener { snapshot ->
                    snapshot.children
                        .mapNotNull { child -> child.getValue(MessageGroupRealtimeEntity::class.java) }
                        .sortedBy { it.sentTime }
                        .let {
                            coroutineScope.launch { messages.emit(it) }
                        }
                }
            )
        return messages.asSharedFlow()
    }

    fun removeMessageListener(clusterId: String, userId: String, groupId: String) {
        messageListener?.let {
            messageDatabase.child(clusterId).child(userId).child(groupId).removeEventListener(it)
        }
        messageListener = null
    }

    private fun sendMessage(
        clusterId: String,
        groupId: String,
        userId: String,
        message: MessageGroupRealtimeEntity
    ) {
        messageDatabase
            .child(clusterId)
            .child(userId)
            .child(groupId)
            .child(message.id.orEmpty())
            .setValue(message)

        groupDatabase
            .child(clusterId)
            .child(userId)
            .child(groupId)
            .child(RealtimeRoot.TOP_MESSAGE_ID)
            .setValue(message.id.orEmpty())
    }
}