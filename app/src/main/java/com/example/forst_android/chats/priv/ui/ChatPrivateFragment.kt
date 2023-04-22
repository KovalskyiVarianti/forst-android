package com.example.forst_android.chats.priv.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.forst_android.R
import com.example.forst_android.common.ui.viewBinding
import com.example.forst_android.databinding.FragmentChatPrivateBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatPrivateFragment : Fragment(R.layout.fragment_chat_private) {
    private val binding: FragmentChatPrivateBinding by viewBinding()

    private val viewModel: ChatPrivateViewModel by viewModels()

    private val adapter by lazy {
        ChatPrivateAdapter { id ->

        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.chatList.adapter = adapter
        adapter.submitList(
            listOf(
                ChatPrivateItem("1","","Somebody", "","Hello"),
                ChatPrivateItem("1","","Somebody", "","Hello"),
                ChatPrivateItem("1","","Somebody", "","Hello"),
                ChatPrivateItem("1","","Somebody", "","Hello"),
                ChatPrivateItem("1","","Somebody", "","Hello"),
            )
        )
    }
}