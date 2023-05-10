package com.example.forst_android.chats.group.list.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

typealias GroupClickListener = (groupId: String) -> Unit

class ChatGroupAdapter(
    private val groupClickListener: GroupClickListener
) : ListAdapter<ChatGroupItem, ChatGroupItemHolder>(ChatGroupItemDiffCallback) {

    companion object ChatGroupItemDiffCallback : DiffUtil.ItemCallback<ChatGroupItem>() {
        override fun areItemsTheSame(oldItem: ChatGroupItem, newItem: ChatGroupItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ChatGroupItem, newItem: ChatGroupItem): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatGroupItemHolder {
        return ChatGroupItemHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ChatGroupItemHolder, position: Int) {
        holder.bind(getItem(position), groupClickListener)
    }
}