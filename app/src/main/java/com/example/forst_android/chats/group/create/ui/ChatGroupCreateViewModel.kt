package com.example.forst_android.chats.group.create.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.forst_android.chats.group.create.domain.CreateChatGroupUseCase
import com.example.forst_android.chats.priv.create.domain.ClusterMembersListenerInteractor
import com.example.forst_android.clusters.data.ClusterPreferences
import com.example.forst_android.common.coroutines.CoroutineDispatchers
import com.example.forst_android.common.domain.service.UserService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private val selectedUsers = mutableSetOf<String>()

    val clusterMembers = clusterMembersListenerInteractor.addClusterMembersListener().map { users ->
        chatGroupUserItemMapper.map(users)
    }

    private val chatClickResult = MutableStateFlow<ChatGroupClickResult?>(null)
    fun getChatClickResult() = chatClickResult.asStateFlow()

    fun createGroup(name: String) {
        viewModelScope.launch(coroutineDispatchers.io) {
            createChatGroupUseCase.createGroup(
                clusterPreferences.getSelectedClusterId().first()
                    ?: throw IllegalArgumentException(),
                userService.userUID ?: throw IllegalArgumentException(),
                name,
                selectedUsers.toList()
            ).let { groupId ->
                chatClickResult.emit(ChatGroupClickResult(groupId))
            }
        }
    }

    fun updateSelection(userId: String, isSelected: Boolean) {
        if (isSelected) {
            selectedUsers.add(userId)
        } else {
            selectedUsers.remove(userId)
        }
    }
}