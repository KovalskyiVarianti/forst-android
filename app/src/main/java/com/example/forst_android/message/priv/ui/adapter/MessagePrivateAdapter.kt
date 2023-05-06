package com.example.forst_android.message.priv.ui.adapter

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.forst_android.message.priv.ui.MessagePrivateItem

class MessagePrivateAdapter :
    ListAdapter<MessagePrivateItem, MessagePrivateItemHolder>(MessagePrivateItemDiffCallback) {

    private companion object MessagePrivateItemDiffCallback :
        DiffUtil.ItemCallback<MessagePrivateItem>() {
        override fun areItemsTheSame(
            oldItem: MessagePrivateItem,
            newItem: MessagePrivateItem
        ): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: MessagePrivateItem,
            newItem: MessagePrivateItem
        ): Boolean {
            return oldItem == newItem
        }
    }

    private enum class ViewType(val type: Int) {
        TEXT(0), IMAGE(1),
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessagePrivateItemHolder {
        return when(ViewType.values().first { it.type == viewType }) {
            ViewType.TEXT -> MessagePrivateTextHolder.from(parent)
            ViewType.IMAGE -> TODO()
        }
    }

    override fun onBindViewHolder(holder: MessagePrivateItemHolder, position: Int) {
        when (holder) {
            is MessagePrivateTextHolder -> holder.bind(getItem(position) as MessagePrivateItem.MessagePrivateText)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is MessagePrivateItem.MessagePrivateText -> ViewType.TEXT.type
        }
    }
}