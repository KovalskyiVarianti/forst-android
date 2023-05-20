package com.example.forst_android.message.group.ui.adapter

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

class MessageGroupAdapter :
    ListAdapter<MessageGroupItem, MessageGroupItemHolder>(MessageGroupItemDiffCallback) {

    private companion object MessageGroupItemDiffCallback :
        DiffUtil.ItemCallback<MessageGroupItem>() {
        override fun areItemsTheSame(
            oldItem: MessageGroupItem,
            newItem: MessageGroupItem
        ): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: MessageGroupItem,
            newItem: MessageGroupItem
        ): Boolean {
            return oldItem == newItem
        }
    }

    private enum class ViewType(val type: Int) {
        TEXT(0), IMAGE(1),
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageGroupItemHolder {
        return when (ViewType.values().first { it.type == viewType }) {
            ViewType.TEXT -> MessageGroupTextHolder.from(parent)
            ViewType.IMAGE -> MessageGroupImageHolder.from(parent)
        }
    }

    override fun onBindViewHolder(holder: MessageGroupItemHolder, position: Int) {
        when (holder) {
            is MessageGroupTextHolder -> holder.bind(getItem(position) as MessageGroupItem.MessageGroupText)
            is MessageGroupImageHolder -> holder.bind(getItem(position) as MessageGroupItem.MessageGroupImage)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is MessageGroupItem.MessageGroupText -> ViewType.TEXT.type
            is MessageGroupItem.MessageGroupImage -> ViewType.IMAGE.type
        }
    }
}