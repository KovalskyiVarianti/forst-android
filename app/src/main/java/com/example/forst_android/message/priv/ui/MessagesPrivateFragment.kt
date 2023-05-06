package com.example.forst_android.message.priv.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.forst_android.R
import com.example.forst_android.common.ui.viewBinding
import com.example.forst_android.databinding.FragmentMessagesPrivateBinding
import com.example.forst_android.message.priv.ui.adapter.MessagePrivateAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MessagesPrivateFragment : Fragment(R.layout.fragment_messages_private) {

    private val binding: FragmentMessagesPrivateBinding by viewBinding()
    private val navArgs: MessagesPrivateFragmentArgs by navArgs()

    private val messagesPrivateViewModel: MessagesPrivateViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.messageList.adapter = MessagePrivateAdapter()

        messagesPrivateViewModel.fetchUserInfo(navArgs.interlocutorId)

        binding.apply {
            sendButton.setOnClickListener {
                messageInput.text.toString().takeIf { it.isNotBlank() }?.let {
                    messagesPrivateViewModel.sendMessage(navArgs.chatId, navArgs.interlocutorId, it)
                }
                messageInput.setText("")
            }
            backButtonIcon.setOnClickListener {
                activity?.onBackPressedDispatcher?.onBackPressed()
            }
        }

        lifecycleScope.launch {
            messagesPrivateViewModel.getMessages(
                navArgs.chatId,
                navArgs.interlocutorId
            ).flowWithLifecycle(lifecycle, Lifecycle.State.CREATED).collect { messages ->
                (binding.messageList.adapter as MessagePrivateAdapter).submitList(messages)
//                binding.messageList.smoothScrollToPosition(messages.size - 1)
            }
        }

        lifecycleScope.launch {
            messagesPrivateViewModel.getUserInfo().flowWithLifecycle(
                lifecycle,
                Lifecycle.State.CREATED
            ).collect { userInfo ->
                userInfo?.let {
                    binding.interlocutorName.text = userInfo.name
                }
            }
        }
    }
}