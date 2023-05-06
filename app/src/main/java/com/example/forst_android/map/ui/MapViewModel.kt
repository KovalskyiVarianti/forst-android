package com.example.forst_android.map.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.forst_android.clusters.data.ClusterPreferences
import com.example.forst_android.common.coroutines.CoroutineDispatchers
import com.example.forst_android.common.domain.service.UserService
import com.example.forst_android.map.data.DefaultLocationService
import com.example.forst_android.map.data.LocationShareStateRepository
import com.example.forst_android.map.domain.ClusterMapUserListenerInteractor
import com.example.forst_android.map.domain.FollowUserLocationUseCase
import com.example.forst_android.map.domain.FollowedUsersListenerInteractor
import com.example.forst_android.map.ui.dialog.ClusterUserMapItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val followUserLocationUseCase: FollowUserLocationUseCase,
    private val followedUsersListenerInteractor: FollowedUsersListenerInteractor,
    private val clusterMapUserListenerInteractor: ClusterMapUserListenerInteractor,
    private val clusterPreferences: ClusterPreferences,
    private val userService: UserService,
    private val mapFollowedItemMapper: MapFollowedItemMapper,
    private val defaultLocationService: DefaultLocationService,
    private val locationShareStateRepository: LocationShareStateRepository,
    private val coroutineDispatchers: CoroutineDispatchers
) : ViewModel() {
    init {
        viewModelScope.launch(coroutineDispatchers.default) {
            defaultLocationService.startSharingLocation()
        }
    }

    val mapFollowedItems = clusterPreferences.getSelectedClusterId().flatMapMerge { clusterId ->
        followedUsersListenerInteractor.addFollowedUsersListener(
            clusterId ?: throw IllegalArgumentException(),
            userService.userUID ?: throw IllegalArgumentException(),
        )
    }.map { users ->
        mapFollowedItemMapper.map(users)
    }

    val clusterMapUsers = clusterPreferences.getSelectedClusterId().flatMapMerge { clusterId ->
        clusterMapUserListenerInteractor.addClusterMapUserListener(
            clusterId ?: throw IllegalArgumentException(),
            userService.userUID ?: throw IllegalArgumentException(),
        )
    }.map { users ->
        users.map { user ->
            ClusterUserMapItem(user.id, user.name, user.imageUrl, user.isFollowed, user.isEnabled)
        }
    }

    val locationShareState = clusterPreferences.getSelectedClusterId().flatMapMerge { clusterId ->
        locationShareStateRepository.isLocationShareEnabled(clusterId.orEmpty())
    }

    fun onFollow(userId: String, isFollowed: Boolean) {
        viewModelScope.launch(coroutineDispatchers.io) {
            followUserLocationUseCase.followUserLocation(
                clusterPreferences.getSelectedClusterId().firstOrNull().orEmpty(),
                userService.userUID ?: throw IllegalArgumentException(),
                userId,
                isFollowed,
            )
        }
    }

    fun changeShareLocationState() {
        viewModelScope.launch(coroutineDispatchers.io) {
            clusterPreferences.getSelectedClusterId().firstOrNull()?.let {
                locationShareStateRepository.changeLocationShareStateForCluster(it)
            }
        }
    }
}