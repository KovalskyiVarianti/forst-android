package com.example.forst_android.message.group.ui.adapter

sealed class MessageGroupItem(
    open val id: String,
    open val sendTime: String,
    open val isSelf: Boolean,
    open val senderName: String,
    open val senderImageUrl: String,
) {
    data class MessageGroupText(
        override val id: String,
        override val sendTime: String,
        override val isSelf: Boolean,
        override val senderName: String,
        override val senderImageUrl: String,
        val text: String,
    ) : MessageGroupItem(id, sendTime, isSelf, senderName, senderImageUrl)

    data class MessageGroupImage(
        override val id: String,
        override val sendTime: String,
        override val isSelf: Boolean,
        override val senderName: String,
        override val senderImageUrl: String,
        val url: String,
    ) : MessageGroupItem(id, sendTime, isSelf, senderName, senderImageUrl)
}