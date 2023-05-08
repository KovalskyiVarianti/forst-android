package com.example.forst_android.events.list.ui.adapter

import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.forst_android.events.list.ui.adapter.holder.AddNewEventItemHolder
import com.example.forst_android.events.list.ui.adapter.holder.DateItemHolder
import com.example.forst_android.events.list.ui.adapter.holder.EventItemHolder
import com.example.forst_android.events.list.ui.adapter.holder.EventListItemHolder

class EventsAdapter(
    private val addEventListener : OnClickListener
) : ListAdapter<EventListItem, EventListItemHolder>(EventListItemDiffCallback) {

    companion object EventListItemDiffCallback : DiffUtil.ItemCallback<EventListItem>() {
        override fun areItemsTheSame(oldItem: EventListItem, newItem: EventListItem): Boolean {
            return if (oldItem is EventListItem.EventItem && newItem is EventListItem.EventItem) {
                oldItem.id == newItem.id
            } else {
                oldItem == newItem
            }
        }

        override fun areContentsTheSame(oldItem: EventListItem, newItem: EventListItem): Boolean {
            return oldItem == newItem
        }
    }

    enum class ViewType(val type: Int) {
        EVENT(0), ADD_EVENT(1), DATE(2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventListItemHolder {
        return when (ViewType.values().first { it.type == viewType }) {
            ViewType.EVENT -> EventItemHolder.from(parent)
            ViewType.ADD_EVENT -> AddNewEventItemHolder.from(parent)
            ViewType.DATE -> DateItemHolder.from(parent)
        }
    }

    override fun onBindViewHolder(holder: EventListItemHolder, position: Int) {
        when (holder) {
            is EventItemHolder -> holder.bind(getItem(position) as EventListItem.EventItem)
            is AddNewEventItemHolder -> holder.bind(addEventListener)
            is DateItemHolder -> holder.bind(getItem(position) as EventListItem.EventDateItem)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is EventListItem.EventItem -> ViewType.EVENT.type
            is EventListItem.EventDateItem -> ViewType.DATE.type
            EventListItem.AddNewEventItem -> ViewType.ADD_EVENT.type
        }
    }
}