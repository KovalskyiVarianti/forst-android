package com.example.forst_android.chats.toggle.ui

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.forst_android.chats.priv.ui.ChatPrivateFragment

class ChatToggleAdapter(parentFragment: Fragment) : FragmentStateAdapter(parentFragment) {

    companion object {
        const val PRIVATE_CHAT_POSITION = 0
        const val GROUP_CHAT_POSITION = 1
        const val CHATS_COUNT = 2
    }

    override fun getItemCount() = CHATS_COUNT

    override fun createFragment(position: Int) = when (position) {
        PRIVATE_CHAT_POSITION -> ChatPrivateFragment()
        GROUP_CHAT_POSITION -> ChatPrivateFragment()
        else -> throw IllegalArgumentException("Only Private and Group chat fragments allowed!")
    }

}