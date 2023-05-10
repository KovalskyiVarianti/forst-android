package com.example.forst_android.chats.group.list.ui

import androidx.lifecycle.ViewModel
import com.example.forst_android.chats.group.list.domain.ChatGroupListenerInteractor
import com.example.forst_android.chats.group.list.ui.adapter.ChatGroupItemMapper
import com.example.forst_android.clusters.data.ClusterPreferences
import com.example.forst_android.common.domain.service.UserService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class ChatGroupViewModel @Inject constructor(
    private val chatGroupListenerInteractor: ChatGroupListenerInteractor,
    private val clusterPreferences: ClusterPreferences,
    private val userService: UserService,
    private val chatGroupItemMapper: ChatGroupItemMapper,
) : ViewModel() {

    val chatGroups = clusterPreferences.getSelectedClusterId().flatMapMerge { clusterId ->
        chatGroupListenerInteractor.addChatListener(
            clusterId.orEmpty(),
            userService.userUID.orEmpty()
        )
    }.map { groups ->
        chatGroupItemMapper.map(groups)
    }


}