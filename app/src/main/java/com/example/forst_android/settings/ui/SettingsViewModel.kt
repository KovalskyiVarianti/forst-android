package com.example.forst_android.settings.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.forst_android.clusters.data.ClusterPreferences
import com.example.forst_android.common.coroutines.CoroutineDispatchers
import com.example.forst_android.common.domain.service.UserService
import com.example.forst_android.settings.domain.GetClusterByIdUseCase
import com.example.forst_android.settings.domain.UpdateClusterPrivacyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getClusterByIdUseCase: GetClusterByIdUseCase,
    private val clusterPreferences: ClusterPreferences,
    private val userService: UserService,
    private val updateClusterPrivacyUseCase: UpdateClusterPrivacyUseCase,
    private val coroutineDispatchers: CoroutineDispatchers,
) : ViewModel() {

    val clusterInfo = clusterPreferences.getSelectedClusterId().map { clusterId ->
        getClusterByIdUseCase.getCluster(clusterId.orEmpty()).let { cluster ->
            ClusterInfo(
                cluster.id,
                cluster.name,
                cluster.isPrivate,
                cluster.ownerId == userService.userUID
            )
        }
    }

    fun updateClusterPrivacy(value: Boolean) {
        viewModelScope.launch(coroutineDispatchers.io) {
            clusterPreferences.getSelectedClusterId().firstOrNull()?.let {
                updateClusterPrivacyUseCase.updateClusterPrivacy(it, value)
            }
        }
    }
}