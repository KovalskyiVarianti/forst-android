package com.example.forst_android.events.list.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.forst_android.R
import com.example.forst_android.common.ui.viewBinding
import com.example.forst_android.databinding.FragmentEventsBinding
import com.example.forst_android.events.list.ui.adapter.EventsAdapter
import com.example.forst_android.home.ui.HomeFragmentDirections
import com.example.forst_android.main.navigation.MainNavigationManager
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

        binding.chatList.adapter = EventsAdapter(
            addEventListener = {
                mainNavigationManager.navigate(
                    lifecycleScope,
                    HomeFragmentDirections.actionHomeFragmentToEventCreateFragment()
                )
            }
        )

        lifecycleScope.launch {
            eventsViewModel.events.flowWithLifecycle(
                lifecycle,
                Lifecycle.State.STARTED
            ).collect { events ->
                (binding.chatList.adapter as EventsAdapter).submitList(events)
            }
        }
    }
}