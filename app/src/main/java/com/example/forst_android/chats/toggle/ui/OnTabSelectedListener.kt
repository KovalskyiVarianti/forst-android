package com.example.forst_android.chats.toggle.ui

import com.google.android.material.tabs.TabLayout

class OnTabSelectedListener(
    val onSelected: (tab: TabLayout.Tab?) -> Unit
): TabLayout.OnTabSelectedListener {
    override fun onTabSelected(tab: TabLayout.Tab?) {
        onSelected(tab)
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
        // nothing
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {
        // nothing
    }
}