package com.example.forst_android.map.ui.adapter

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.forst_android.common.ui.ItemClickListener

class MapFollowedAdapter(
    private val onAddFollowedUserListener: () -> Unit,
    private val onUserClick: ItemClickListener,
) : ListAdapter<MapFollowedItem, MapFollowedItemHolder>(UserFollowedItemDiffCallback) {

    private companion object UserFollowedItemDiffCallback :
        DiffUtil.ItemCallback<MapFollowedItem>() {
        override fun areItemsTheSame(
            oldItem: MapFollowedItem,
            newItem: MapFollowedItem
        ): Boolean {
            return if (oldItem is MapFollowedItem.UserMapFollowedItem && newItem is MapFollowedItem.UserMapFollowedItem) {
                oldItem.id == newItem.id
            } else {
                oldItem == newItem
            }
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: MapFollowedItem,
            newItem: MapFollowedItem
        ): Boolean {
            return if (oldItem is MapFollowedItem.UserMapFollowedItem && newItem is MapFollowedItem.UserMapFollowedItem) {
                oldItem.id == newItem.id && oldItem.name == newItem.name && oldItem.imageUrl == newItem.imageUrl
            } else {
                oldItem == newItem
            }
        }
    }

    enum class ViewType(val type: Int) {
        USER(0), ADD_USER(1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MapFollowedItemHolder {
        return when (ViewType.values().first { it.type == viewType }) {
            ViewType.USER -> MapFollowedUserItemHolder.from(parent)
            ViewType.ADD_USER -> AddFollowedUserButtonItemHolder.from(parent)
        }
    }

    override fun onBindViewHolder(holder: MapFollowedItemHolder, position: Int) {
        when (holder) {
            is MapFollowedUserItemHolder -> holder.bind(
                getItem(position) as MapFollowedItem.UserMapFollowedItem,
                onUserClick,
            )
            is AddFollowedUserButtonItemHolder -> holder.bind(onAddFollowedUserListener)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is MapFollowedItem.UserMapFollowedItem -> ViewType.USER.type
            is MapFollowedItem.AddFollowedUserButton -> ViewType.ADD_USER.type
        }
    }
}