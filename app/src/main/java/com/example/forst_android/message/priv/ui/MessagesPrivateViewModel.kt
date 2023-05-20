package com.example.forst_android.message.priv.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.forst_android.clusters.data.ClusterPreferences
import com.example.forst_android.common.coroutines.CoroutineDispatchers
import com.example.forst_android.common.domain.service.UserService
import com.example.forst_android.message.priv.domain.*
import com.example.forst_android.message.priv.ui.adapter.MessagePrivateItemMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.InputStream
import javax.inject.Inject

@HiltViewModel
class MessagesPrivateViewModel @Inject constructor(
    private val messageSendUseCase: MessageSendUseCase,
    private val messageListenerInteractor: MessageListenerInteractor,
    private val userService: UserService,
    private val clusterPreferences: ClusterPreferences,
    private val coroutineDispatchers: CoroutineDispatchers,
    private val messagePrivateItemMapper: MessagePrivateItemMapper,
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val photoSendUseCase: PhotoSendUseCase,
) : ViewModel() {

    private val userInfo = MutableStateFlow<UserInfo?>(null)
    fun getUserInfo() = userInfo.asStateFlow()

    fun fetchUserInfo(interlocutorId: String) {
        viewModelScope.launch(coroutineDispatchers.io) {
            getUserByIdUseCase.getUser(interlocutorId).let { user ->
                userInfo.emit(
                    UserInfo(
                        user.id,
                        user.name.takeIf { it.isNotBlank() } ?: user.phoneNumber,
                        user.photoUri
                    )
                )
            }
        }
    }

    fun getMessages(chatId: String, interlocutorId: String): Flow<List<MessagePrivateItem>> {
        val selfId = userService.userUID ?: throw IllegalArgumentException()
        return clusterPreferences.getSelectedClusterId().flatMapMerge { clusterId ->
            messageListenerInteractor.addMessageListener(
                clusterId ?: throw IllegalArgumentException(),
                interlocutorId,
                chatId
            )
        }.map { messages ->
            messagePrivateItemMapper.map(messages, selfId)
        }
    }

    fun sendMessage(chatId: String, interlocutorId: String, message: String) {
        viewModelScope.launch(coroutineDispatchers.io) {
            val clusterId = clusterPreferences.getSelectedClusterId().firstOrNull()
                ?: throw IllegalArgumentException()
            val userId = userService.userUID ?: throw IllegalArgumentException()
            messageSendUseCase.sendMessage(
                clusterId,
                chatId,
                userId,
                interlocutorId,
                message
            )
        }
    }

    fun sendPhoto(chatId: String, interlocutorId: String, stream: InputStream) {
        viewModelScope.launch(coroutineDispatchers.io) {
            val clusterId = clusterPreferences.getSelectedClusterId().firstOrNull()
                ?: throw IllegalArgumentException()
            val userId = userService.userUID ?: throw IllegalArgumentException()
            photoSendUseCase.sendPhoto(
                clusterId,
                chatId,
                userId,
                interlocutorId,
                stream,
            )
        }
    }
}