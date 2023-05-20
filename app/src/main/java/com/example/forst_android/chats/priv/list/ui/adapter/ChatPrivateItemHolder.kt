package com.example.forst_android.chats.priv.list.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.forst_android.R
import com.example.forst_android.common.ui.loadAvatar
import com.example.forst_android.databinding.ItemChatPrivateBinding
import com.example.forst_android.message.priv.domain.MessageType

class ChatPrivateItemHolder(private val binding: ItemChatPrivateBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: ChatPrivateItem, chatClickListener: ChatClickListener) {
        binding.apply {
            interlocutorName.text = item.personName
            when {
                item.topMessageText.isBlank() -> {
                    topMessage.text = root.context.getString(R.string.no_messages_yet)
                    topMessage.isVisible = true
                    messageImage.isVisible = false
                }
                item.topMessageType == MessageType.TEXT -> {
                    topMessage.text = item.topMessageText
                    topMessage.isVisible = true
                    messageImage.isVisible = false
                }
                item.topMessageType == MessageType.IMAGE -> {
                    Glide.with(root)
                        .load(item.topMessageText)
                        .centerCrop()
                        .error(R.drawable.icon_warning)
                        .into(messageImage)
                    messageImage.isVisible = true
                    topMessage.isVisible = false
                }
            }
            topMessageSentTime.text = item.topMessageSentTime
            root.setOnClickListener { chatClickListener(item.id, item.personUID) }
            interlocutorImage.loadAvatar(item.personPhoto)
        }
    }

    companion object {
        fun from(parent: ViewGroup) = ChatPrivateItemHolder(
            ItemChatPrivateBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }
}