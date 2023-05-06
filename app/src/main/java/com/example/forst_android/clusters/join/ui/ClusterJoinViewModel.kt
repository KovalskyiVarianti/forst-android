package com.example.forst_android.clusters.join.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.forst_android.clusters.join.domain.ClusterAllListenerInteractor
import com.example.forst_android.clusters.join.domain.ClusterJoinUseCase
import com.example.forst_android.common.coroutines.CoroutineDispatchers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClusterJoinViewModel @Inject constructor(
    private val clusterJoinUseCase: ClusterJoinUseCase,
    private val clusterAllListenerInteractor: ClusterAllListenerInteractor,
    private val clusterJoinItemsMapper: ClusterJoinItemsMapper,
    private val coroutineDispatchers: CoroutineDispatchers,
) : ViewModel() {

    private val clusterJoinResult =
        MutableStateFlow<ClusterJoinState>(ClusterJoinState.Joining)

    fun getClusterJoinResult() = clusterJoinResult.asStateFlow()

    fun joinCluster(id: String?) {
        if (id.isNullOrBlank()) {
            clusterJoinResult.value = ClusterJoinState.Error("Invalid id")
            return
        }
        viewModelScope.launch(coroutineDispatchers.io) {
            clusterJoinUseCase.joinCluster(id)
            clusterJoinResult.value = ClusterJoinState.Success
        }
    }

    fun getAllClusters() = clusterAllListenerInteractor.addAllClustersListener().map { clusterEntities ->
        clusterJoinItemsMapper.map(clusterEntities)
    }

    override fun onCleared() {
        clusterAllListenerInteractor.removeAllClustersListener()
        super.onCleared()
    }
}