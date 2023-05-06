package com.example.forst_android.chats.priv.create.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.forst_android.chats.priv.create.domain.ClusterMembersListenerInteractor
import com.example.forst_android.chats.priv.create.domain.GetChatPrivateIdUseCase
import com.example.forst_android.common.coroutines.CoroutineDispatchers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatPrivateCreateViewModel @Inject constructor(
    private val clusterMembersListenerInteractor: ClusterMembersListenerInteractor,
    private val getChatPrivateIdUseCase: GetChatPrivateIdUseCase,
    private val chatPrivateUserItemMapper: ChatPrivateUserItemMapper,
    private val coroutineDispatchers: CoroutineDispatchers,
) : ViewModel() {

    val clusterMembers = clusterMembersListenerInteractor.addClusterMembersListener().map { users ->
        chatPrivateUserItemMapper.map(users)
    }

    private val chatClickResult = MutableStateFlow<ChatPrivateClickResult?>(null)
    fun getChatClickResult() = chatClickResult.asStateFlow()

    fun onChatClick(interlocutorId: String) {
        viewModelScope.launch(coroutineDispatchers.io) {
            getChatPrivateIdUseCase.getChatId(interlocutorId).let { chatId ->
                chatClickResult.emit(ChatPrivateClickResult(chatId, interlocutorId))
            }
        }
    }

    override fun onCleared() {
        viewModelScope.launch(coroutineDispatchers.io) {
            clusterMembersListenerInteractor.removeClusterMembersListener()
        }
        super.onCleared()
    }
}