package com.example.forst_android.chats.priv.list.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.forst_android.R
import com.example.forst_android.chats.priv.list.ui.adapter.ChatPrivateAdapter
import com.example.forst_android.common.ui.viewBinding
import com.example.forst_android.databinding.FragmentChatPrivateBinding
import com.example.forst_android.home.ui.HomeFragmentDirections
import com.example.forst_android.main.navigation.MainNavigationManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ChatPrivateFragment : Fragment(R.layout.fragment_chat_private) {

    private val binding: FragmentChatPrivateBinding by viewBinding()

    private val chatPrivateViewModel: ChatPrivateViewModel by viewModels()

    @Inject
    lateinit var mainNavigationManager: MainNavigationManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.chatList.adapter = ChatPrivateAdapter { chatId, interlocutorId ->
            mainNavigationManager.navigate(
                lifecycleScope,
                HomeFragmentDirections.actionHomeFragmentToMessagesPrivateFragment(
                    chatId,
                    interlocutorId
                )
            )
        }

        lifecycleScope.launch {
            chatPrivateViewModel.chats.flowWithLifecycle(
                lifecycle,
            ).collect { chats ->
                (binding.chatList.adapter as ChatPrivateAdapter).submitList(chats)
            }
        }
    }
}