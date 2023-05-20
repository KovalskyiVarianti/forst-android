package com.example.forst_android.chats.priv.create.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.forst_android.R
import com.example.forst_android.chats.priv.create.ui.adapter.ChatPrivateUserAdapter
import com.example.forst_android.common.ui.viewBinding
import com.example.forst_android.databinding.FragmentChatPrivateCreateBinding
import com.example.forst_android.main.navigation.MainNavigationManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ChatPrivateCreateFragment : Fragment(R.layout.fragment_chat_private_create) {

    private val binding: FragmentChatPrivateCreateBinding by viewBinding()

    private val chatPrivateCreateViewModel: ChatPrivateCreateViewModel by viewModels()

    @Inject
    lateinit var mainNavigationManager: MainNavigationManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.userList.adapter = ChatPrivateUserAdapter { interlocutorId ->
            chatPrivateCreateViewModel.onChatClick(interlocutorId)
        }

        lifecycleScope.launch {
            chatPrivateCreateViewModel.clusterMembers.flowWithLifecycle(
                lifecycle,
                Lifecycle.State.STARTED
            ).collect { users ->
                (binding.userList.adapter as ChatPrivateUserAdapter).submitList(users)
            }
        }

        lifecycleScope.launch {
            chatPrivateCreateViewModel.getChatClickResult().flowWithLifecycle(
                lifecycle,
                Lifecycle.State.CREATED
            ).collect { result ->
                result?.let {
                    mainNavigationManager.navigate(
                        lifecycleScope,
                        ChatPrivateCreateFragmentDirections.actionChatPrivateCreateFragmentToMessagesPrivateFragment(
                            result.chatId,
                            result.interlocutorId
                        )
                    )
                }
            }
        }
    }
}