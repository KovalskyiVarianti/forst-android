package com.example.forst_android.chats.group.create.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.forst_android.R
import com.example.forst_android.chats.group.create.ui.adapter.ChatGroupUserAdapter
import com.example.forst_android.common.ui.viewBinding
import com.example.forst_android.databinding.FragmentChatGroupCreateBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChatGroupCreateFragment : Fragment(R.layout.fragment_chat_group_create) {

    private val binding: FragmentChatGroupCreateBinding by viewBinding()

    private val chatGroupCreateViewModel: ChatGroupCreateViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.userList.adapter = ChatGroupUserAdapter()

        lifecycleScope.launch {
            chatGroupCreateViewModel.clusterMembers.flowWithLifecycle(
                lifecycle,
                Lifecycle.State.CREATED
            ).collect { users ->
                (binding.userList.adapter as ChatGroupUserAdapter).submitList(users)

                binding.groupCreateButton.setOnClickListener {
                    val name = binding.groupNameInput.text.toString()
                    chatGroupCreateViewModel.createGroup(
                        name,
                        users.filter { it.isSelected }.map { it.id })
                }
            }
        }
    }
}