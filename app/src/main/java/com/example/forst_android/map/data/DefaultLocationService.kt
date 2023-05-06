package com.example.forst_android.map.data

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.example.forst_android.common.domain.service.UserService
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class DefaultLocationService @Inject constructor(
    @ApplicationContext private val context: Context,
    private val locationRealtimeDatabase: UserLocationRealtimeDatabase,
    private val userService: UserService,
    private val locationShareStateRepository: LocationShareStateRepository,
) {

    private companion object {
        const val DEFAULT_UPDATE_RATE = 3
    }

    suspend fun startSharingLocation() {
        tickerFlow(DEFAULT_UPDATE_RATE.seconds).map {
            locationShareStateRepository.getAllClusterShareStates()
        }.onEach { clusters ->
            clusters.forEach { cluster ->
                val isShare = cluster.isLocationShareEnabled
                val location = isShare.takeIf { it }?.let { getCurrentLocation() }
                val updateTime = System.currentTimeMillis()
                locationRealtimeDatabase.sendLocation(
                    cluster.id,
                    userService.userUID.orEmpty(),
                    location?.latitude,
                    location?.longitude,
                    cluster.isLocationShareEnabled,
                    updateTime
                )
            }
        }.collect()
    }

    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocation(): Location? = suspendCoroutine { continuation ->
        LocationServices.getFusedLocationProviderClient(context).lastLocation
            .addOnSuccessListener(continuation::resume)
            .addOnFailureListener { continuation.resume(null) }
    }

    private fun tickerFlow(period: Duration, initialDelay: Duration = Duration.ZERO) = flow {
        delay(initialDelay)
        while (true) {
            emit(Unit)
            delay(period)
        }
    }
}