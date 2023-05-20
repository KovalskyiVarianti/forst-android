package com.example.forst_android.map.data

import android.content.Context
import android.location.Location
import com.example.forst_android.common.domain.service.UserService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class DefaultLocationService @Inject constructor(
    @ApplicationContext private val context: Context,
    private val locationRealtimeDatabase: UserLocationRealtimeDatabase,
    private val userService: UserService,
    private val locationShareStateRepository: LocationShareStateRepository,
) {

    private val currentLocation = MutableStateFlow<Location?>(null)

    init {
        GpsMyLocationProvider(context).apply {
            locationUpdateMinTime = TimeUnit.SECONDS.toMillis(5)
            startLocationProvider { location, _ ->
                currentLocation.value = location
            }
        }
    }

    suspend fun startSharingLocation() {
        currentLocation.collect { location ->
            locationShareStateRepository.getAllClusterShareStates().forEach { cluster ->
                val updateTime = System.currentTimeMillis()
                val isSharing = cluster.isLocationShareEnabled
                if (isSharing && location == null) {
                    return@forEach
                }
                locationRealtimeDatabase.sendLocation(
                    cluster.id,
                    userService.userUID.orEmpty(),
                    location?.latitude?.takeIf { cluster.isLocationShareEnabled },
                    location?.longitude?.takeIf { cluster.isLocationShareEnabled },
                    cluster.isLocationShareEnabled,
                    updateTime
                )
            }
        }
    }
}