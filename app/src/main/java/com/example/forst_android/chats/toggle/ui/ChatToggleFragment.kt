package com.example.forst_android.chats.toggle.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.forst_android.R
import com.example.forst_android.chats.toggle.ui.ChatToggleAdapter.Companion.GROUP_CHAT_POSITION
import com.example.forst_android.chats.toggle.ui.ChatToggleAdapter.Companion.PRIVATE_CHAT_POSITION
import com.example.forst_android.common.ui.viewBinding
import com.example.forst_android.databinding.FragmentChatToggleBinding
import com.example.forst_android.home.ui.HomeFragmentDirections
import com.example.forst_android.main.navigation.MainNavigationManager
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ChatToggleFragment : Fragment(R.layout.fragment_chat_toggle) {

    private val binding: FragmentChatToggleBinding by viewBinding()

    @Inject
    lateinit var mainNavigationManager: MainNavigationManager

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
                            setOnClickListener {
                                mainNavigationManager.navigate(
                                    lifecycleScope,
                                    HomeFragmentDirections.actionHomeFragmentToChatPrivateCreateFragment()
                                )
                            }
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