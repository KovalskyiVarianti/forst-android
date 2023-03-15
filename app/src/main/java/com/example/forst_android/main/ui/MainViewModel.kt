package com.example.forst_android.main.ui

import androidx.lifecycle.ViewModel
import com.example.forst_android.common.coroutines.CoroutineDispatchers
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val coroutineDispatchers: CoroutineDispatchers
) : ViewModel() {
    // TODO: Implement the ViewModel
}