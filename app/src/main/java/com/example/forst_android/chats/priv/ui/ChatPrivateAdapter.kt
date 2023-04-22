package com.example.forst_android.chats.priv.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.forst_android.common.ui.ItemClickListener

class ChatPrivateAdapter(private val chatClickListener: ItemClickListener) :
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