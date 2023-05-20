package com.example.forst_android.message.priv.ui.adapter

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.constraintlayout.widget.Guideline
import androidx.core.view.setMargins
import com.bumptech.glide.Glide
import com.example.forst_android.R
import com.example.forst_android.databinding.ItemMessagePrivateImageBinding
import com.example.forst_android.message.priv.ui.MessagePrivateItem

class MessagePrivateImageHolder(
    private val binding: ItemMessagePrivateImageBinding
) : MessagePrivateItemHolder(binding.root) {

    fun bind(item: MessagePrivateItem.MessagePrivateImage) = binding.apply {
        Glide.with(root)
            .load(item.url)
            .centerCrop()
            .error(R.drawable.icon_warning)
            .into(messageImage)
        messageCardView.setParamsBySender(item.isSelf)
        messageGuideline.setPercentBySender(item.isSelf)
        messageSendTime.text = item.sendTime
        root.setConstraintsBySender(item.isSelf)
    }

    private fun CardView.setParamsBySender(isSelf: Boolean) {
        layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            this.gravity = if (isSelf) Gravity.END else Gravity.START
            setMargins(7)
        }
    }

    private fun Guideline.setPercentBySender(isSelf: Boolean) {
        setGuidelinePercent(if (isSelf) 0.3F else 0.7F)
    }

    private fun ConstraintLayout.setConstraintsBySender(isSelf: Boolean) {
        ConstraintSet().apply {
            clone(this@setConstraintsBySender)
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
        }.applyTo(this)
    }

    companion object {
        fun from(parent: ViewGroup) = MessagePrivateImageHolder(
            ItemMessagePrivateImageBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }
}