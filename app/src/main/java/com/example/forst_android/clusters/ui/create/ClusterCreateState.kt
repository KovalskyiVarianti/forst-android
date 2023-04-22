package com.example.forst_android.clusters.ui.create

sealed interface ClusterCreateState {
    object Creating : ClusterCreateState
    object Success : ClusterCreateState
    data class Error(val message: String) : ClusterCreateState
}