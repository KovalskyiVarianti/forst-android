package com.example.forst_android.message.group.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.constraintlayout.widget.Guideline
import androidx.core.view.isGone
import com.example.forst_android.R
import com.example.forst_android.databinding.ItemMessageGroupTextBinding

class MessageGroupTextHolder(
    private val binding: ItemMessageGroupTextBinding
) : MessageGroupItemHolder(binding.root) {
    fun bind(item: MessageGroupItem.MessageGroupText) = binding.apply {
        personName.isGone = item.isSelf
        senderImage.isGone = item.isSelf
        messageText.text = item.text
        personName.text = item.senderName
        messageGuideline.setPercentBySender(item.isSelf)
        messageSendTime.text = item.sendTime
        root.setRootConstraintsBySender(item.isSelf)
        messageFrame.setMessageConstraintsBySender(item.isSelf)
    }

    private fun Guideline.setPercentBySender(isSelf: Boolean) {
        setGuidelinePercent(if (isSelf) 0.3F else 0.7F)
    }

    private fun ConstraintLayout.setMessageConstraintsBySender(isSelf: Boolean) {
        ConstraintSet().apply {
            clone(this@setMessageConstraintsBySender)

            connect(
                R.id.senderImage,
                if (isSelf) ConstraintSet.END else ConstraintSet.START,
                R.id.messageFrame,
                if (isSelf) ConstraintSet.END else ConstraintSet.START
            )

            clear(R.id.senderImage, if (isSelf) ConstraintSet.START else ConstraintSet.END)

            connect(
                R.id.messageCardView,
                if (isSelf) ConstraintSet.END else ConstraintSet.START,
                R.id.senderImage,
                if (isSelf) ConstraintSet.START else ConstraintSet.END
            )

            clear(R.id.messageCardView, if (isSelf) ConstraintSet.START else ConstraintSet.END)

        }.applyTo(this)
    }

    private fun ConstraintLayout.setRootConstraintsBySender(isSelf: Boolean) {
        ConstraintSet().apply {
            clone(this@setRootConstraintsBySender)
            connect(
                R.id.messageFrame,
                if (isSelf) ConstraintSet.START else ConstraintSet.END,
                R.id.messageGuideline,
                if (isSelf) ConstraintSet.END else ConstraintSet.START
            )
            connect(
                R.id.messageFrame,
                if (isSelf) ConstraintSet.END else ConstraintSet.START,
                R.id.messageConstraintParent,
                if (isSelf) ConstraintSet.END else ConstraintSet.START,
            )

            connect(
                R.id.messageSendTime,
                if (isSelf) ConstraintSet.END else ConstraintSet.START,
                R.id.messageGuideline,
                if (isSelf) ConstraintSet.START else ConstraintSet.END
            )
            connect(
                R.id.messageSendTime,
                if (isSelf) ConstraintSet.START else ConstraintSet.END,
                R.id.messageConstraintParent,
                if (isSelf) ConstraintSet.START else ConstraintSet.END,
            )

            connect(
                R.id.senderImage,
                if (isSelf) ConstraintSet.START else ConstraintSet.END,
                R.id.messageConstraintParent,
                if (isSelf) ConstraintSet.START else ConstraintSet.END
            )

            connect(
                R.id.messageCardView,
                if (isSelf) ConstraintSet.END else ConstraintSet.START,
                R.id.senderImage,
                if (isSelf) ConstraintSet.START else ConstraintSet.END
            )

        }.applyTo(this)
    }

    companion object {
        fun from(parent: ViewGroup) = MessageGroupTextHolder(
            ItemMessageGroupTextBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }
}