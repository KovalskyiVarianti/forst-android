package com.example.forst_android.chats.group.create.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.forst_android.databinding.ItemChatGroupUserBinding

class ChatGroupUserItemHolder(private val binding: ItemChatGroupUserBinding) :
    ViewHolder(binding.root) {

    fun bind(
        item: ChatGroupUserItem,
        onSelectionChange: (userId: String, isSelected: Boolean) -> Unit
    ) = binding.apply {
        userName.text = item.name.takeIf { it.isNotBlank() } ?: item.phone
        selectionStateButton.setOnCheckedChangeListener { _, isChecked ->
            onSelectionChange(item.id, isChecked)
        }
    }

    companion object {
        fun from(parent: ViewGroup) = ChatGroupUserItemHolder(
            ItemChatGroupUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }
}