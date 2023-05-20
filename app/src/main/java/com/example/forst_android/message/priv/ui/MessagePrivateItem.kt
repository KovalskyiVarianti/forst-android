package com.example.forst_android.message.priv.ui

sealed class MessagePrivateItem(
    open val id: String,
    open val sendTime: String,
    open val isSelf: Boolean,
) {
    data class MessagePrivateText(
        override val id: String,
        override val sendTime: String,
        override val isSelf: Boolean,
        val text: String,
    ) : MessagePrivateItem(id, sendTime, isSelf)

    data class MessagePrivateImage(
        override val id: String,
        override val sendTime: String,
        override val isSelf: Boolean,
        val url: String,
    ) : MessagePrivateItem(id, sendTime, isSelf)
}