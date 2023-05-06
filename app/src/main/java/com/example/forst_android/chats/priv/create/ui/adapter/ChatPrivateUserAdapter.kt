package com.example.forst_android.chats.priv.create.ui.adapter

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.forst_android.common.ui.ItemClickListener

class ChatPrivateUserAdapter(private val itemClickListener: ItemClickListener) :
    ListAdapter<ChatPrivateUserItem, ChatPrivateUserItemHolder>(
        ChatPrivateUserItemDiffCallback
    ) {

    private companion object ChatPrivateUserItemDiffCallback :
        DiffUtil.ItemCallback<ChatPrivateUserItem>() {
        override fun areItemsTheSame(
            oldItem: ChatPrivateUserItem,
            newItem: ChatPrivateUserItem
        ): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: ChatPrivateUserItem,
            newItem: ChatPrivateUserItem
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatPrivateUserItemHolder {
        return ChatPrivateUserItemHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ChatPrivateUserItemHolder, position: Int) {
        holder.bind(getItem(position), itemClickListener)
    }
}