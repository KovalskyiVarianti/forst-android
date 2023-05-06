package com.example.forst_android.chats.priv.list.ui

import androidx.lifecycle.ViewModel
import com.example.forst_android.chats.priv.list.domain.ChatPrivateListenerInteractor
import com.example.forst_android.chats.priv.list.ui.adapter.ChatPrivateItemMapper
import com.example.forst_android.clusters.data.ClusterPreferences
import com.example.forst_android.common.domain.service.UserService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class ChatPrivateViewModel @Inject constructor(
    private val chatPrivateListenerInteractor: ChatPrivateListenerInteractor,
    private val clusterPreferences: ClusterPreferences,
    private val userService: UserService,
    private val chatPrivateItemMapper: ChatPrivateItemMapper,
) : ViewModel() {

    val chats = clusterPreferences.getSelectedClusterId().flatMapMerge { clusterId ->
        chatPrivateListenerInteractor.addChatListener(
            clusterId.orEmpty(),
            userService.userUID.orEmpty()
        )
    }.map { chats ->
        chatPrivateItemMapper.map(chats, userService.userUID ?: throw IllegalArgumentException())
    }
}