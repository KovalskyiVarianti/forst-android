package com.example.forst_android.clusters.ui.dropdown

typealias ClusterDropdownOnSelectedListener = (id: String) -> Unit

data class ClusterDropdownData(
    val selectedCluster: ClusterPopupItem,
    val otherClusters: List<ClusterPopupItem>,
    val onSelected: ClusterDropdownOnSelectedListener
)