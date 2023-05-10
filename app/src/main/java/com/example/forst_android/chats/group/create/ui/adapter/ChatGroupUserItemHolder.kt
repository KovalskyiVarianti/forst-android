package com.example.forst_android.chats.group.create.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.forst_android.databinding.ItemChatGroupUserBinding

class ChatGroupUserItemHolder(private val binding: ItemChatGroupUserBinding) :
    ViewHolder(binding.root) {

    fun bind(item: ChatGroupUserItem) = binding.apply {
        userName.text = item.name.takeIf { it.isNotBlank() } ?: item.phone
        selectionStateButton.setOnCheckedChangeListener { _, isChecked ->
            item.isSelected = isChecked
        }
    }

    companion object {
        fun from(parent: ViewGroup) = ChatGroupUserItemHolder(
            ItemChatGroupUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }
}