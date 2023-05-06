package com.example.forst_android.chats.priv.list.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

typealias ChatClickListener = (chatId: String, interlocutorId: String) -> Unit

class ChatPrivateAdapter(private val chatClickListener: ChatClickListener) :
    ListAdapter<ChatPrivateItem, ChatPrivateItemHolder>(ChatItemDiffCallback) {

    companion object ChatItemDiffCallback : DiffUtil.ItemCallback<ChatPrivateItem>() {
        override fun areItemsTheSame(oldItem: ChatPrivateItem, newItem: ChatPrivateItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ChatPrivateItem, newItem: ChatPrivateItem): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatPrivateItemHolder {
        return ChatPrivateItemHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ChatPrivateItemHolder, position: Int) {
        holder.bind(getItem(position), chatClickListener)
    }
}