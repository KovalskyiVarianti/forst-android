package com.example.forst_android.message.group.ui

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
import com.example.forst_android.databinding.FragmentMessagesGroupBinding
import com.example.forst_android.message.group.ui.adapter.MessageGroupAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MessagesGroupFragment : Fragment(R.layout.fragment_messages_group) {

    private val binding: FragmentMessagesGroupBinding by viewBinding()

    private val navArgs: MessagesGroupFragmentArgs by navArgs()

    private val messagesGroupViewModel: MessagesGroupViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.messageList.adapter = MessageGroupAdapter()

        messagesGroupViewModel.fetchGroupInfo(navArgs.groupId)

        binding.apply {
            sendButton.setOnClickListener {
                messageInput.text.toString().takeIf { it.isNotBlank() }?.let {
                    messagesGroupViewModel.sendMessage(navArgs.groupId, it)
                }
                messageInput.setText("")
            }
            backButtonIcon.setOnClickListener {
                activity?.onBackPressedDispatcher?.onBackPressed()
            }
        }

        lifecycleScope.launch {
            messagesGroupViewModel.getMessages(
                navArgs.groupId
            ).flowWithLifecycle(lifecycle, Lifecycle.State.CREATED).collect { messages ->
                (binding.messageList.adapter as MessageGroupAdapter).submitList(messages)
                if (messages.size > 1) {
                    binding.messageList.smoothScrollToPosition(messages.size - 1)
                }
            }
        }

        lifecycleScope.launch {
            messagesGroupViewModel.getGroupInfo().flowWithLifecycle(
                lifecycle,
                Lifecycle.State.CREATED
            ).collect { groupInfo ->
                binding.groupName.text = groupInfo?.name
            }
        }
    }
}