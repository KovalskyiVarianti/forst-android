package com.example.forst_android.auth.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.forst_android.common.coroutines.CoroutineDispatchers
import com.example.forst_android.main.domain.IsJoinedClustersExistUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val isJoinedClustersExistUseCase: IsJoinedClustersExistUseCase,
    private val coroutineDispatchers: CoroutineDispatchers,
) : ViewModel() {
    private val _verificationId = MutableStateFlow<String?>(null)
    val verificationId: StateFlow<String?> = _verificationId.asStateFlow()

    private val isHomeAvailable = MutableStateFlow<Boolean?>(null)

    fun onCodeSent(verificationId: String) {
        _verificationId.value = verificationId
    }

    fun getHomeAvailability(): StateFlow<Boolean?> {
        viewModelScope.launch(coroutineDispatchers.io) {
            isHomeAvailable.emit(isJoinedClustersExistUseCase.isJoinedClustersExist())
        }
        return isHomeAvailable.asStateFlow()
    }
}