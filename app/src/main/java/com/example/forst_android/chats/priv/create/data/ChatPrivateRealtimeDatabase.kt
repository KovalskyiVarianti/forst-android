package com.example.forst_android.chats.priv.create.data

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

class ChatPrivateRealtimeDatabase @Inject constructor(
    realtimeDatabase: FirebaseDatabase,
    coroutineDispatchers: CoroutineDispatchers
) {

    private val chatsDatabase = realtimeDatabase.getReference(RealtimeRoot.CHATS)

    private val job = Job()
    private val coroutineScope = CoroutineScope(coroutineDispatchers.io + job)

    private val chatsPrivate = MutableSharedFlow<List<ChatPrivateRealtimeEntity>>(
        1,
        0,
        BufferOverflow.DROP_OLDEST
    )

    private var chatPrivateEventListener: ValueEventListener? = null

    private var lastClusterId: String? = null
    private var lastUserId: String? = null

    suspend fun getPrivateChatId(clusterId: String, selfId: String, interlocutorId: String) =
        suspendCancellableCoroutine {
            chatsDatabase.child(clusterId).child(selfId).addListenerForSingleValueEvent(
                DataEventListener { dataSnapshot ->
                    dataSnapshot.children.mapNotNull { child ->
                        child.getValue(ChatPrivateRealtimeEntity::class.java)
                    }.firstOrNull { chat ->
                        chat.interlocutorId == interlocutorId
                    }.let { chat ->
                        it.resume(chat?.id)
                    }
                }
            )
        }

    fun createPrivateChat(
        clusterId: String,
        chatId: String,
        selfId: String,
        interlocutorId: String
    ) {
        createChatForUser(clusterId, chatId, selfId, interlocutorId)
        createChatForUser(clusterId, chatId, interlocutorId, selfId)
    }

    fun addChatPrivateListener(
        clusterId: String,
        userId: String
    ): SharedFlow<List<ChatPrivateRealtimeEntity>> {
        removeChatPrivateListener()
        lastClusterId = clusterId
        lastUserId = userId
        chatPrivateEventListener = chatsDatabase
            .child(clusterId)
            .child(userId)
            .addValueEventListener(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.children.mapNotNull { child ->
                            child.getValue(
                                ChatPrivateRealtimeEntity::class.java
                            )
                        }.let { coroutineScope.launch { chatsPrivate.emit(it) } }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Timber.d("New data ${error.message}")
                    }

                }
            )
        return chatsPrivate.asSharedFlow()
    }

    fun removeChatPrivateListener() {
        chatPrivateEventListener?.let {
            lastClusterId?.let { clusterId ->
                lastUserId?.let { userId ->
                    chatsDatabase.child(clusterId).child(userId).removeEventListener(it)
                }
            }
        }
        chatPrivateEventListener = null
    }

    private fun createChatForUser(
        clusterId: String,
        chatId: String,
        userId: String,
        interlocutorId: String,
    ) {
        chatsDatabase
            .child(clusterId)
            .child(userId)
            .child(chatId)
            .setValue(
                ChatPrivateRealtimeEntity(id = chatId, interlocutorId = interlocutorId)
            )
    }
}