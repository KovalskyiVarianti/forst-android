package com.example.forst_android.home.navigation

import androidx.lifecycle.LifecycleCoroutineScope
import androidx.navigation.NavDirections
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeNavigationManager @Inject constructor() {

    private val navigationRequest = MutableSharedFlow<NavDirections>(
        replay = 1,
        extraBufferCapacity = 0,
        onBufferOverflow = BufferOverflow.DROP_LATEST
    )

    internal fun getNavigationRequest(): SharedFlow<NavDirections> =
        navigationRequest.asSharedFlow()

    fun navigate(lifecycleScope: LifecycleCoroutineScope, direction: NavDirections) {
        lifecycleScope.launch {
            navigationRequest.emit(direction)
        }
    }
}