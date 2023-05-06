package com.example.forst_android.map.ui.dialog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.forst_android.databinding.ItemMapUserDialogBinding

class ClusterUserMapItemHolder(private val binding: ItemMapUserDialogBinding) :
    ViewHolder(binding.root) {

    fun bind(item: ClusterUserMapItem, onFollow: (userId: String, isFollowed: Boolean) -> Unit) =
        binding.apply {
            userName.text = item.name
            checkbox.isVisible = item.isEnabled
            userStateText.isVisible = item.isEnabled.not()
            checkbox.isChecked = item.isSelected
            checkbox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked != item.isSelected) {
                    onFollow(item.id, isChecked)
                }
            }
        }

    companion object {
        fun from(parent: ViewGroup) = ClusterUserMapItemHolder(
            ItemMapUserDialogBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }
}