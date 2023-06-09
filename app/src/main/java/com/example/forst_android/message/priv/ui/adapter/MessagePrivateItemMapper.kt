package com.example.forst_android.message.priv.ui.adapter

import com.example.forst_android.message.priv.domain.MessagePrivateEntity
import com.example.forst_android.message.priv.domain.MessageType
import com.example.forst_android.message.priv.ui.MessagePrivateItem
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class MessagePrivateItemMapper @Inject constructor() {

    private val formatter by lazy { SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()) }

    fun map(messageEntities: List<MessagePrivateEntity>, selfId: String): List<MessagePrivateItem> {
        return messageEntities.map { message ->
            when (message.type) {
                MessageType.IMAGE -> MessagePrivateItem.MessagePrivateImage(
                    message.id,
                    formatter.format(message.sendTime),
                    message.senderId == selfId,
                    message.data
                )
                else -> MessagePrivateItem.MessagePrivateText(
                    message.id,
                    formatter.format(message.sendTime),
                    message.senderId == selfId,
                    message.data
                )
            }
        }
    }
}