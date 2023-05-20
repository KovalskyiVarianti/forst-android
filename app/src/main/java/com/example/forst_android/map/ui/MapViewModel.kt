package com.example.forst_android.map.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.forst_android.clusters.data.ClusterPreferences
import com.example.forst_android.common.coroutines.CoroutineDispatchers
import com.example.forst_android.common.domain.service.UserService
import com.example.forst_android.map.data.DefaultLocationService
import com.example.forst_android.map.data.DefaultPointsListenerInteractor
import com.example.forst_android.map.data.LocationShareStateRepository
import com.example.forst_android.map.domain.ClusterMapUserListenerInteractor
import com.example.forst_android.map.domain.CreatePointUseCase
import com.example.forst_android.map.domain.FollowUserLocationUseCase
import com.example.forst_android.map.domain.FollowedUsersListenerInteractor
import com.example.forst_android.map.ui.dialog.ClusterUserMapItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
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
    private val createPointUseCase: CreatePointUseCase,
    private val pointsListenerInteractor: DefaultPointsListenerInteractor,
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

    val points = clusterPreferences.getSelectedClusterId().flatMapMerge { clusterId ->
        pointsListenerInteractor.addPointsListener(clusterId.orEmpty())
    }

    val locationShareState = clusterPreferences.getSelectedClusterId().flatMapMerge { clusterId ->
        locationShareStateRepository.isLocationShareEnabled(clusterId.orEmpty())
    }

    private val locationCreateMode = MutableStateFlow(false)
    fun getLocationCreateMode() = locationCreateMode.asStateFlow()

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

    fun updateCreationModeState(value: Boolean) {
        locationCreateMode.value = value
    }

    fun createPoint(name: String, latitude: Double, longitude: Double) {
        viewModelScope.launch(coroutineDispatchers.io) {
            clusterPreferences.getSelectedClusterId().firstOrNull()?.let { clusterId ->
                userService.userUID?.let { userId ->
                    createPointUseCase.createPoint(clusterId, userId, name, latitude, longitude)
                }
            }
        }
    }

    override fun onCleared() {
        clusterMapUserListenerInteractor.removeClusterMapUserListener()
        followedUsersListenerInteractor.removeFollowedUsersListener()
        pointsListenerInteractor.removePointsListener()
        super.onCleared()
    }
}