package com.example.forst_android.events.list.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.forst_android.R
import com.example.forst_android.chats.toggle.ui.ZoomOutPageTransformer
import com.example.forst_android.common.ui.viewBinding
import com.example.forst_android.databinding.FragmentEventsBinding
import com.example.forst_android.home.ui.HomeFragmentDirections
import com.example.forst_android.main.navigation.MainNavigationManager
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class EventsFragment : Fragment(R.layout.fragment_events) {

    private val binding: FragmentEventsBinding by viewBinding()

    private val eventsViewModel: EventsViewModel by viewModels()

    @Inject
    lateinit var mainNavigationManager: MainNavigationManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.pager.setPageTransformer(ZoomOutPageTransformer())
        binding.pager.adapter = EventsPageAdapter(
            addEventListener = {
                mainNavigationManager.navigate(
                    lifecycleScope,
                    HomeFragmentDirections.actionHomeFragmentToEventCreateFragment()
                )
            }
        )

        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            tab.text = when (position) {
                EventsPageAdapter.ACTIVE_EVENTS_POSITION -> getString(R.string.active)
                EventsPageAdapter.ENDED_EVENTS_POSITION -> getString(R.string.ended)
                else -> throw IllegalArgumentException("Only Private and Group chat fragments allowed!")
            }
        }.attach()

        lifecycleScope.launch {
            eventsViewModel.eventsPages.flowWithLifecycle(
                lifecycle,
                Lifecycle.State.STARTED
            ).collect { pages ->
                (binding.pager.adapter as EventsPageAdapter).submitPages(pages)
            }
        }
    }
}