package com.example.forst_android.clusters.join.ui

sealed interface ClusterJoinState {
    object Joining : ClusterJoinState
    object Success : ClusterJoinState
    data class Error(val message: String) : ClusterJoinState
}