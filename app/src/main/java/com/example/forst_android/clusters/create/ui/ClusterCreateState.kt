package com.example.forst_android.clusters.create.ui

sealed interface ClusterCreateState {
    object Creating : ClusterCreateState
    object Success : ClusterCreateState
    data class Error(val message: String) : ClusterCreateState
}