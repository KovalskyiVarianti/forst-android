package com.example.forst_android.chats.group.list.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.forst_android.R
import com.example.forst_android.common.ui.loadAvatar
import com.example.forst_android.databinding.ItemChatGroupBinding
import com.example.forst_android.message.priv.domain.MessageType

class ChatGroupItemHolder(private val binding: ItemChatGroupBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: ChatGroupItem, groupClickListener: GroupClickListener) = binding.apply {
        groupName.text = item.name
        when {
            item.topMessageText.isBlank() -> {
                topMessage.text = root.context.getString(R.string.no_messages_yet)
                topMessage.isVisible = true
                messageImage.isVisible = false
                topMessageSenderImage.isVisible = false
            }
            item.topMessageType == MessageType.TEXT -> {
                topMessage.text = item.topMessageText
                topMessage.isVisible = true
                messageImage.isVisible = false
                topMessageSenderImage.isVisible = true
            }
            item.topMessageType == MessageType.IMAGE -> {
                Glide.with(root)
                    .load(item.topMessageText)
                    .centerCrop()
                    .error(R.drawable.icon_warning)
                    .into(messageImage)
                messageImage.isVisible = true
                topMessage.isVisible = false
                topMessageSenderImage.isVisible = true
            }
        }
        topMessageSenderName.text = item.topMessageSenderName
        topMessageSenderImage.loadAvatar(item.topMessageSenderImage)
        topMessageSentTime.text = item.topMessageSentTime
        root.setOnClickListener { groupClickListener(item.id) }
    }

    companion object {
        fun from(parent: ViewGroup) = ChatGroupItemHolder(
            ItemChatGroupBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }
}