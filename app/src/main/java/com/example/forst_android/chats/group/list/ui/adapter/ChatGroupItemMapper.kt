package com.example.forst_android.chats.group.list.ui.adapter

import com.example.forst_android.chats.group.list.domain.ChatGroupEntity
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class ChatGroupItemMapper @Inject constructor() {

    private val formatter by lazy { SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()) }


    fun map(chatGroupEntities: List<ChatGroupEntity>): List<ChatGroupItem> {
        return chatGroupEntities.map { chatGroupEntity ->
            ChatGroupItem(
                chatGroupEntity.id,
                chatGroupEntity.name,
                chatGroupEntity.topMessageSenderName,
                chatGroupEntity.topMessageSenderImage,
                chatGroupEntity.topMessageText.orEmpty(),
                chatGroupEntity.topMessageType,
                chatGroupEntity.topMessageSentTime?.let { formatter.format(it) }.orEmpty()
            )
        }
    }
}