package com.example.forst_android.chats.priv.ui

import androidx.lifecycle.ViewModel
import com.example.forst_android.common.domain.service.UserService
import javax.inject.Inject

class ChatPrivateViewModel @Inject constructor(
    private val userService: UserService
) : ViewModel() {

}