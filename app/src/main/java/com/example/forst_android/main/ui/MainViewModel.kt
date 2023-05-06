package com.example.forst_android.main.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.forst_android.common.coroutines.CoroutineDispatchers
import com.example.forst_android.common.domain.service.UserService
import com.example.forst_android.main.domain.IsJoinedClustersExistUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userService: UserService,
    private val isJoinedClustersExistUseCase: IsJoinedClustersExistUseCase,
    private val coroutineDispatchers: CoroutineDispatchers,
) : ViewModel() {

    private val entryPoint = MutableStateFlow<MainEntryPoint>(MainEntryPoint.Splash)

    fun getEntryPoint(): StateFlow<MainEntryPoint> {
        viewModelScope.launch(coroutineDispatchers.io) {
            if (userService.isLoggedIn) {
                entryPoint.value = if (isJoinedClustersExistUseCase.isJoinedClustersExist()) {
                    MainEntryPoint.Home
                } else {
                    MainEntryPoint.ClusterEntry
                }
            } else {
                entryPoint.value = MainEntryPoint.Login
            }
        }
        return entryPoint.asStateFlow()
    }
}