package com.example.forst_android.chats.group.create.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.forst_android.chats.group.create.domain.CreateChatGroupUseCase
import com.example.forst_android.chats.priv.create.domain.ClusterMembersListenerInteractor
import com.example.forst_android.clusters.data.ClusterPreferences
import com.example.forst_android.common.coroutines.CoroutineDispatchers
import com.example.forst_android.common.domain.service.UserService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatGroupCreateViewModel @Inject constructor(
    private val createChatGroupUseCase: CreateChatGroupUseCase,
    private val clusterMembersListenerInteractor: ClusterMembersListenerInteractor,
    private val chatGroupUserItemMapper: ChatGroupUserItemMapper,
    private val clusterPreferences: ClusterPreferences,
    private val userService: UserService,
    private val coroutineDispatchers: CoroutineDispatchers,
) : ViewModel() {

    val clusterMembers = clusterMembersListenerInteractor.addClusterMembersListener().map { users ->
        chatGroupUserItemMapper.map(users)
    }

    fun createGroup(name: String, membersIds: List<String>) {
        viewModelScope.launch(coroutineDispatchers.io) {
            createChatGroupUseCase.createGroup(
                clusterPreferences.getSelectedClusterId().first()
                    ?: throw IllegalArgumentException(),
                userService.userUID ?: throw IllegalArgumentException(),
                name,
                membersIds
            )
        }
    }
}