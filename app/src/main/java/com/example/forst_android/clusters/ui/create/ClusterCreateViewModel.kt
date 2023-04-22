package com.example.forst_android.clusters.ui.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.forst_android.clusters.data.DefaultClustersRepository
import com.example.forst_android.common.coroutines.CoroutineDispatchers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClusterCreateViewModel @Inject constructor(
    private val clustersRepository: DefaultClustersRepository,
    private val coroutineDispatchers: CoroutineDispatchers,
) : ViewModel() {

    private val clusterCreateResult =
        MutableStateFlow<ClusterCreateState>(ClusterCreateState.Creating)
    fun getClusterCreateResult() = clusterCreateResult.asStateFlow()

    fun createCluster(name: String?) {
        if (name.isNullOrBlank()) {
            clusterCreateResult.value = ClusterCreateState.Error("Invalid name")
            return
        }
        viewModelScope.launch(coroutineDispatchers.io) {
            clustersRepository.createCluster(name)
            clusterCreateResult.value = ClusterCreateState.Success
        }
    }
}