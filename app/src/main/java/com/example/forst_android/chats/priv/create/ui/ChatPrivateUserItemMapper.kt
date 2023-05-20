package com.example.forst_android.chats.priv.create.ui

import com.example.forst_android.chats.priv.create.ui.adapter.ChatPrivateUserItem
import com.example.forst_android.common.domain.entity.UserEntity
import com.example.forst_android.common.domain.service.UserService
import javax.inject.Inject

class ChatPrivateUserItemMapper @Inject constructor(
    private val userService: UserService,
) {
    fun map(userEntities: List<UserEntity>): List<ChatPrivateUserItem> {
        return userEntities.map { user ->
            ChatPrivateUserItem(
                user.id,
                user.name,
                user.phoneNumber,
                user.photoUri,
                user.id == userService.userUID
            )
        }
    }
}