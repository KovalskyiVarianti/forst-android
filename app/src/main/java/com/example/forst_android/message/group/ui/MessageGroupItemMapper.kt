package com.example.forst_android.message.group.ui

import com.example.forst_android.message.group.domain.MessageGroupEntity
import com.example.forst_android.message.group.ui.adapter.MessageGroupItem
import com.example.forst_android.message.priv.domain.GetUserByIdUseCase
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class MessageGroupItemMapper @Inject constructor(
    private val getUserByIdUseCase: GetUserByIdUseCase,
) {

    private val formatter by lazy { SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()) }

    suspend fun map(messages: List<MessageGroupEntity>, userId: String): List<MessageGroupItem> {
        return messages.map { message ->
            val user = getUserByIdUseCase.getUser(message.senderId)
            MessageGroupItem.MessageGroupText(
                message.id,
                formatter.format(message.sendTime),
                user.id == userId,
                user.name.takeIf { it.isNotBlank() } ?: user.phoneNumber,
                user.photoUri,
                message.data
            )
        }
    }
}