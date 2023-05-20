package com.example.forst_android.chats.priv.create.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.forst_android.R
import com.example.forst_android.common.ui.ItemClickListener
import com.example.forst_android.common.ui.loadAvatar
import com.example.forst_android.databinding.ItemChatPrivateUserBinding

class ChatPrivateUserItemHolder(private val binding: ItemChatPrivateUserBinding) :
    ViewHolder(binding.root) {

    fun bind(item: ChatPrivateUserItem, itemClickListener: ItemClickListener) = binding.apply {
        root.setOnClickListener {
            itemClickListener(item.id)
        }
        userName.text = item.name.takeIf { it.isNotBlank() } ?: item.phone
        userImage.loadAvatar(item.photo)
        createChatText.text = when (item.isSelf) {
            true -> root.context.getString(R.string.open_chat_you)
            false -> root.context.getString(R.string.open_chat)
        }
    }

    companion object {
        fun from(parent: ViewGroup) = ChatPrivateUserItemHolder(
            ItemChatPrivateUserBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }
}