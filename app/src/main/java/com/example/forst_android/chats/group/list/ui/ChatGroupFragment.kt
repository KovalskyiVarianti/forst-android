package com.example.forst_android.chats.group.list.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.forst_android.R
import com.example.forst_android.chats.group.list.ui.adapter.ChatGroupAdapter
import com.example.forst_android.common.ui.viewBinding
import com.example.forst_android.databinding.FragmentChatGroupBinding
import com.example.forst_android.home.ui.HomeFragmentDirections
import com.example.forst_android.main.navigation.MainNavigationManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ChatGroupFragment : Fragment(R.layout.fragment_chat_group) {

    private val binding: FragmentChatGroupBinding by viewBinding()

    private val chatGroupViewModel: ChatGroupViewModel by viewModels()

    @Inject
    lateinit var mainNavigationManager: MainNavigationManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.chatList.adapter = ChatGroupAdapter { groupId ->
            mainNavigationManager.navigate(
                lifecycleScope,
                HomeFragmentDirections.actionHomeFragmentToMessagesGroupFragment(groupId)
            )
        }

        lifecycleScope.launch {
            chatGroupViewModel.chatGroups.flowWithLifecycle(
                lifecycle,
                Lifecycle.State.STARTED
            ).collect { groups ->
                (binding.chatList.adapter as ChatGroupAdapter).submitList(groups)
                binding.emptyListMessage.isVisible = groups.isEmpty()
            }
        }
    }
}