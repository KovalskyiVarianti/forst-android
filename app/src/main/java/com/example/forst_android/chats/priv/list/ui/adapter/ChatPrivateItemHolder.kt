package com.example.forst_android.chats.priv.list.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.forst_android.databinding.ItemChatPrivateBinding

class ChatPrivateItemHolder(private val binding: ItemChatPrivateBinding) : RecyclerView.ViewHolder(binding.root) {
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
            ItemChatPrivateBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }
}