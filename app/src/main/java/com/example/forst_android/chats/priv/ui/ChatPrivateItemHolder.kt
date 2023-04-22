package com.example.forst_android.chats.priv.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.forst_android.common.ui.ItemClickListener
import com.example.forst_android.databinding.ItemChatBinding

class ChatPrivateItemHolder(private val binding: ItemChatBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: ChatPrivateItem, chatClickListener: ItemClickListener) {
        binding.apply {
            personName.text = item.personName
            chatTopMessage.text = item.topMessage
            root.setOnClickListener { chatClickListener(item.id) }
        }
    }

    companion object {
        fun from(parent: ViewGroup) = ChatPrivateItemHolder(
            ItemChatBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }
}