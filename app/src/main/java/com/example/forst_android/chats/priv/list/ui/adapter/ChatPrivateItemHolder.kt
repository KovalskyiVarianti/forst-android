package com.example.forst_android.chats.priv.list.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.forst_android.databinding.ItemChatBinding

class ChatPrivateItemHolder(private val binding: ItemChatBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: ChatPrivateItem, chatClickListener: ChatClickListener) {
        binding.apply {
            interlocutorName.text = item.personName
            topMessage.text = item.topMessageText
            topMessageSentTime.text = item.topMessageSentTime
            root.setOnClickListener { chatClickListener(item.id, item.personUID) }
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