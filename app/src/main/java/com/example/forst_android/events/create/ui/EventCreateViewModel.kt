package com.example.forst_android.events.create.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.forst_android.clusters.data.ClusterPreferences
import com.example.forst_android.common.coroutines.CoroutineDispatchers
import com.example.forst_android.events.create.domain.CreateEventUseCase
import com.example.forst_android.map.domain.PointsListenerInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class EventCreateViewModel @Inject constructor(
    private val coroutineDispatchers: CoroutineDispatchers,
    private val eventCreateEntityMapper: EventCreateEntityMapper,
    private val createEventUseCase: CreateEventUseCase,
    private val clusterPreferences: ClusterPreferences,
    private val pointsListenerInteractor: PointsListenerInteractor,
) : ViewModel() {

    private val selectedEventLocation = MutableStateFlow<EventLocation?>(null)
    fun getSelectedEventLocation() = selectedEventLocation.asStateFlow()

    val points = clusterPreferences.getSelectedClusterId().flatMapMerge { clusterId ->
        pointsListenerInteractor.addPointsListener(clusterId.orEmpty())
    }.map { points -> points.map { point -> PointItem(point.id, point.name) } }

    fun createEvent(data: EventCreateData) {
        viewModelScope.launch(coroutineDispatchers.io) {
            createEventUseCase.createEvent(
                clusterPreferences.getSelectedClusterId().first()
                    ?: throw IllegalArgumentException(),
                eventCreateEntityMapper.map(data)
            )
        }
    }

    fun selectEventLocation(locationName: String, location: String) {
        selectedEventLocation.value = EventLocation(locationName, location)
    }

    override fun onCleared() {
        pointsListenerInteractor.removePointsListener()
        super.onCleared()
    }
}