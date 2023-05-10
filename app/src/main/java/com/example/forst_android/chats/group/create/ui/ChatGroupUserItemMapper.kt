package com.example.forst_android.chats.group.create.ui

import com.example.forst_android.chats.group.create.ui.adapter.ChatGroupUserItem
import com.example.forst_android.common.domain.entity.UserEntity
import com.example.forst_android.common.domain.service.UserService
import javax.inject.Inject

class ChatGroupUserItemMapper @Inject constructor(
    private val userService: UserService,
) {
    fun map(userEntities: List<UserEntity>): List<ChatGroupUserItem> {
        return userEntities.mapNotNull { user ->
            if (user.id == userService.userUID) return@mapNotNull null
            ChatGroupUserItem(
                user.id,
                user.name,
                user.phoneNumber,
                false
            )
        }
    }
}