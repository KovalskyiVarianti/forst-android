package com.example.forst_android.chats.group.create.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

class ChatGroupUserAdapter(
    private val onSelectionChange: (userId: String, isSelected: Boolean) -> Unit
) : ListAdapter<ChatGroupUserItem, ChatGroupUserItemHolder>(ChatGroupUserItemDiffCallback) {

    private companion object ChatGroupUserItemDiffCallback :
        DiffUtil.ItemCallback<ChatGroupUserItem>() {
        override fun areItemsTheSame(
            oldItem: ChatGroupUserItem,
            newItem: ChatGroupUserItem
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ChatGroupUserItem,
            newItem: ChatGroupUserItem
        ): Boolean {
            return oldItem == newItem
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatGroupUserItemHolder {
        return ChatGroupUserItemHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ChatGroupUserItemHolder, position: Int) {
        holder.bind(getItem(position), onSelectionChange)
    }
}