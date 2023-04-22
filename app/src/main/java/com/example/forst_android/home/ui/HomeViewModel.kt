package com.example.forst_android.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.forst_android.clusters.data.DefaultClustersRepository
import com.example.forst_android.clusters.ui.dropdown.ClusterDropdownDataMapper
import com.example.forst_android.common.coroutines.CoroutineDispatchers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val clustersRepository: DefaultClustersRepository,
    private val clusterDropdownDataMapper: ClusterDropdownDataMapper,
    private val coroutineDispatchers: CoroutineDispatchers,
) : ViewModel() {

    val clusterDropdownData = clustersRepository.getClusters().map { clusterEntities ->
        clusterDropdownDataMapper.map(clusterEntities) { clusterId ->
            viewModelScope.launch(coroutineDispatchers.io) {
                clustersRepository.selectCluster(clusterId)
            }
        }
    }
}

