package com.example.forst_android.message.group.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.forst_android.clusters.data.ClusterPreferences
import com.example.forst_android.common.coroutines.CoroutineDispatchers
import com.example.forst_android.common.domain.service.UserService
import com.example.forst_android.message.group.domain.GetChatGroupByIdUseCase
import com.example.forst_android.message.group.domain.MessageGroupListenerInteractor
import com.example.forst_android.message.group.domain.PhotoGroupSendUseCase
import com.example.forst_android.message.group.domain.SendGroupMessageUseCase
import com.example.forst_android.message.group.ui.adapter.MessageGroupItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.InputStream
import javax.inject.Inject

@HiltViewModel
class MessagesGroupViewModel @Inject constructor(
    private val clusterPreferences: ClusterPreferences,
    private val userService: UserService,
    private val sendGroupMessageUseCase: SendGroupMessageUseCase,
    private val getChatGroupByIdUseCase: GetChatGroupByIdUseCase,
    private val messageGroupListenerInteractor: MessageGroupListenerInteractor,
    private val messageGroupItemMapper: MessageGroupItemMapper,
    private val coroutineDispatchers: CoroutineDispatchers,
    private val photoGroupSendUseCase: PhotoGroupSendUseCase,
) : ViewModel() {

    private val groupInfo = MutableStateFlow<GroupInfo?>(null)
    fun getGroupInfo() = groupInfo.asStateFlow()

    fun getMessages(groupId: String): Flow<List<MessageGroupItem>> {
        val userId = userService.userUID.orEmpty()
        return clusterPreferences.getSelectedClusterId().flatMapMerge { clusterId ->
            messageGroupListenerInteractor.addMessageListener(
                clusterId.orEmpty(),
                userId,
                groupId
            )
        }.map { messages ->
            messageGroupItemMapper.map(messages, userId)
        }
    }

    fun sendMessage(groupId: String, text: String) {
        viewModelScope.launch(coroutineDispatchers.io) {
            clusterPreferences.getSelectedClusterId().firstOrNull()?.let { clusterId ->
                userService.userUID?.let { userId ->
                    sendGroupMessageUseCase.sendMessage(
                        clusterId,
                        groupId,
                        userId,
                        getChatGroupByIdUseCase.getChatGroupById(
                            clusterId,
                            userId,
                            groupId
                        ).members,
                        text
                    )
                }
            }
        }
    }

    fun fetchGroupInfo(groupId: String) {
        viewModelScope.launch(coroutineDispatchers.io) {
            clusterPreferences.getSelectedClusterId().firstOrNull()?.let { clusterId ->
                userService.userUID?.let { userId ->
                    getChatGroupByIdUseCase.getChatGroupById(clusterId, userId, groupId).apply {
                        groupInfo.emit(GroupInfo(id, name, ""))
                    }
                }
            }
        }
    }

    fun sendPhoto(groupId: String, stream: InputStream) {
        viewModelScope.launch(coroutineDispatchers.io) {
            clusterPreferences.getSelectedClusterId().firstOrNull()?.let { clusterId ->
                userService.userUID?.let { userId ->
                    photoGroupSendUseCase.sendPhoto(
                        clusterId,
                        groupId,
                        userId,
                        getChatGroupByIdUseCase.getChatGroupById(
                            clusterId,
                            userId,
                            groupId
                        ).members,
                        stream,
                    )
                }
            }
        }
    }
}