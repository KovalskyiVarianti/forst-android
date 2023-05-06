package com.example.forst_android.message.priv.data

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

class MessageRealtimeDatabase @Inject constructor(
    private val realtimeDatabase: FirebaseDatabase,
    private val coroutineDispatchers: CoroutineDispatchers,
) {

    private val job = Job()
    private val coroutineScope = CoroutineScope(coroutineDispatchers.io + job)

    private val messageDatabase = realtimeDatabase.getReference(RealtimeRoot.MESSAGES)

    private val chatDatabase = realtimeDatabase.getReference(RealtimeRoot.CHATS)

    private val messages = MutableSharedFlow<List<MessageRealtimeEntity>>(
        1,
        0,
        BufferOverflow.DROP_OLDEST
    )

    private var messageListener: ValueEventListener? = null

    suspend fun getMessageById(
        clusterId: String,
        userId: String,
        chatId: String,
        messageId: String
    ) = suspendCancellableCoroutine {
        messageDatabase
            .child(clusterId)
            .child(userId)
            .child(chatId)
            .child(messageId)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        it.resume(snapshot.getValue(MessageRealtimeEntity::class.java))
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Timber.d("New data ${error.message}")
                    }
                }
            )
    }

    fun sendMessage(
        clusterId: String,
        chatId: String,
        userId: String,
        interlocutorId: String,
        message: MessageRealtimeEntity
    ) {
        sendMessage(clusterId, chatId, userId, message)
        sendMessage(clusterId, chatId, interlocutorId, message)
    }

    fun addMessageListener(
        clusterId: String,
        userId: String,
        chatId: String
    ): SharedFlow<List<MessageRealtimeEntity>> {
        removeMessageListener(clusterId, userId, chatId)
        messageListener = messageDatabase
            .child(clusterId)
            .child(userId)
            .child(chatId)
            .addValueEventListener(
                DataEventListener { snapshot ->
                    snapshot.children
                        .mapNotNull { child -> child.getValue(MessageRealtimeEntity::class.java) }
                        .sortedBy { it.sentTime }
                        .let {
                            coroutineScope.launch { messages.emit(it) }
                        }
                }
            )
        return messages.asSharedFlow()
    }

    fun removeMessageListener(clusterId: String, userId: String, chatId: String) {
        messageListener?.let {
            messageDatabase.child(clusterId).child(userId).child(chatId).removeEventListener(it)
        }
        messageListener = null
    }

    private fun sendMessage(
        clusterId: String,
        chatId: String,
        userId: String,
        message: MessageRealtimeEntity
    ) {
        messageDatabase
            .child(clusterId)
            .child(userId)
            .child(chatId)
            .child(message.id.orEmpty())
            .setValue(message)

        chatDatabase
            .child(clusterId)
            .child(userId)
            .child(chatId)
            .child(RealtimeRoot.TOP_MESSAGE_ID)
            .setValue(message.id.orEmpty())
    }
}