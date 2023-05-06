package com.example.forst_android.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.forst_android.clusters.data.ClusterPreferences
import com.example.forst_android.clusters.ui.dropdown.ClusterDropdownDataMapper
import com.example.forst_android.common.coroutines.CoroutineDispatchers
import com.example.forst_android.home.domain.ClusterJoinedListenerInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val clusterJoinedListenerInteractor: ClusterJoinedListenerInteractor,
    private val clusterPreferences: ClusterPreferences,
    private val clusterDropdownDataMapper: ClusterDropdownDataMapper,
    private val coroutineDispatchers: CoroutineDispatchers,
) : ViewModel() {

    val clusterDropdownData = clusterJoinedListenerInteractor.addJoinedClustersListener().combine(
        clusterPreferences.getSelectedClusterId()
    ) { clusters, selectedId ->
        clusterDropdownDataMapper.map(clusters, selectedId) { clusterId ->
            viewModelScope.launch(coroutineDispatchers.io) {
                clusterPreferences.selectCluster(clusterId)
            }
        }
    }

    override fun onCleared() {
        clusterJoinedListenerInteractor.removeJoinedClustersListener()
        super.onCleared()
    }
}

