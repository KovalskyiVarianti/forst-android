package com.example.forst_android.events.list.ui

import androidx.lifecycle.ViewModel
import com.example.forst_android.clusters.data.ClusterPreferences
import com.example.forst_android.events.list.domain.EventsListenerInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class EventsViewModel @Inject constructor(
    private val eventsListenerInteractor: EventsListenerInteractor,
    private val clusterPreferences: ClusterPreferences,
    private val eventListItemMapper: EventListItemMapper,
) : ViewModel() {

    val events = clusterPreferences.getSelectedClusterId().flatMapMerge { clusterId ->
        eventsListenerInteractor.addEventsListener(clusterId.orEmpty())
    }.map { events ->
        eventListItemMapper.map(events)

    }
}