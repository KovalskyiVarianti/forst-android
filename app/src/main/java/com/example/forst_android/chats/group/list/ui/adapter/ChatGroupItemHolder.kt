package com.example.forst_android.chats.group.list.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.forst_android.databinding.ItemChatGroupBinding

class ChatGroupItemHolder(private val binding: ItemChatGroupBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: ChatGroupItem, groupClickListener: GroupClickListener) = binding.apply {
        groupName.text = item.name
        topMessage.text = item.topMessageText
        topMessageSenderName.text = item.topMessageSenderName
        topMessageSentTime.text = item.topMessageSentTime
        root.setOnClickListener { groupClickListener(item.id) }
    }

    companion object {
        fun from(parent: ViewGroup) = ChatGroupItemHolder(
            ItemChatGroupBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }
}