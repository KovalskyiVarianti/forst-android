package com.example.forst_android.chats.toggle.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import com.example.forst_android.R
import com.example.forst_android.chats.toggle.ui.ChatToggleAdapter.Companion.GROUP_CHAT_POSITION
import com.example.forst_android.chats.toggle.ui.ChatToggleAdapter.Companion.PRIVATE_CHAT_POSITION
import com.example.forst_android.common.ui.viewBinding
import com.example.forst_android.databinding.FragmentChatToggleBinding
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatToggleFragment : Fragment(R.layout.fragment_chat_toggle) {

    private val binding: FragmentChatToggleBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.pager.adapter = ChatToggleAdapter(this)
        binding.pager.setPageTransformer(ZoomOutPageTransformer())

        binding.tabLayout.addOnTabSelectedListener(
            OnTabSelectedListener { tab ->
                binding.addChatButton.apply {
                    when (tab?.position) {
                        PRIVATE_CHAT_POSITION -> {
                            setImageDrawable(
                                AppCompatResources.getDrawable(context, R.drawable.icon_add_private)
                            )
                        }
                        GROUP_CHAT_POSITION -> {
                            setImageDrawable(
                                AppCompatResources.getDrawable(context, R.drawable.icon_add_group)
                            )
                        }
                        else -> throw IllegalArgumentException("Only Private and Group chat fragments allowed!")
                    }
                }
            }
        )
        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            tab.text = when (position) {
                PRIVATE_CHAT_POSITION -> getString(R.string.privates)
                GROUP_CHAT_POSITION -> getString(R.string.groups)
                else -> throw IllegalArgumentException("Only Private and Group chat fragments allowed!")
            }
        }.attach()
    }
}