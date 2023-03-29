package com.example.forst_android.main.navigation

import androidx.navigation.NavDirections
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigationManager @Inject constructor() {

    private val navigationRequest = MutableStateFlow<NavDirections?>(null)
    internal fun getNavigationRequest(): StateFlow<NavDirections?> = navigationRequest.asStateFlow()

    fun navigate(direction: NavDirections) {
        navigationRequest.value = direction
    }
}