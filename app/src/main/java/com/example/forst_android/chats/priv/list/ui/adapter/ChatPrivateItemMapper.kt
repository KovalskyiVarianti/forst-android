package com.example.forst_android.chats.priv.list.ui.adapter

import com.example.forst_android.chats.priv.list.domain.ChatPrivateEntity
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class ChatPrivateItemMapper @Inject constructor() {

    private val formatter by lazy { SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()) }

    fun map(chatEntities: List<ChatPrivateEntity>, selfId: String): List<ChatPrivateItem> {
        return chatEntities.map { chat ->
            ChatPrivateItem(
                chat.id,
                chat.interlocutorId,
                if (chat.interlocutorId == selfId) "You" else chat.interlocutorName,
                chat.interlocutorImageUrl,
                chat.topMessageText.orEmpty(),
                chat.topMessageType,
                chat.topMessageSentTime?.let { formatter.format(it) }.orEmpty()
            )
        }
    }
}