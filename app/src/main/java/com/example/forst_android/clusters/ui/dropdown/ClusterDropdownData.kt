package com.example.forst_android.clusters.ui.dropdown

import com.example.forst_android.clusters.ui.dropdown.adapter.ClusterPopupItem
import com.example.forst_android.common.ui.ItemClickListener

data class ClusterDropdownData(
    val selectedCluster: ClusterPopupItem.ClusterItem,
    val otherClusters: List<ClusterPopupItem>,
    val onSelected: ItemClickListener
)