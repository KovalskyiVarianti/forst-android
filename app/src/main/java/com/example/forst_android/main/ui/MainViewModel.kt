package com.example.forst_android.main.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.forst_android.common.coroutines.CoroutineDispatchers
import com.example.forst_android.common.domain.service.UserService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userService: UserService,
    private val coroutineDispatchers: CoroutineDispatchers,
) : ViewModel() {

    private val entryPoint = MutableStateFlow<MainEntryPoint>(MainEntryPoint.Splash)

    fun getEntryPoint(): StateFlow<MainEntryPoint> {
        viewModelScope.launch(coroutineDispatchers.main) {
            entryPoint.value = if (userService.isLoggedIn) {
                MainEntryPoint.Main
            } else {
                MainEntryPoint.Login
            }
        }
        return entryPoint.asStateFlow()
    }
}