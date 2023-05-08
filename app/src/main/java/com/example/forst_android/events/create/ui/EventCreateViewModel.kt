package com.example.forst_android.events.create.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.forst_android.clusters.data.ClusterPreferences
import com.example.forst_android.common.coroutines.CoroutineDispatchers
import com.example.forst_android.events.create.domain.CreateEventUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class EventCreateViewModel @Inject constructor(
    private val coroutineDispatchers: CoroutineDispatchers,
    private val eventCreateEntityMapper: EventCreateEntityMapper,
    private val createEventUseCase: CreateEventUseCase,
    private val clusterPreferences: ClusterPreferences,
) : ViewModel() {

    fun createEvent(data: EventCreateData) {
        viewModelScope.launch(coroutineDispatchers.io) {
            createEventUseCase.createEvent(
                clusterPreferences.getSelectedClusterId().first()
                    ?: throw IllegalArgumentException(),
                eventCreateEntityMapper.map(data)
            )
        }
    }
}