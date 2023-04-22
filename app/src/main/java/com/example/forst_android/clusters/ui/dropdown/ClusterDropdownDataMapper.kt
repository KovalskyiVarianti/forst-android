package com.example.forst_android.clusters.ui.dropdown

import com.example.forst_android.clusters.domain.ClusterEntity
import com.example.forst_android.common.ui.ItemClickListener
import javax.inject.Inject

class ClusterDropdownDataMapper @Inject constructor() {
    fun map(
        clusterEntities: List<ClusterEntity>,
        onSelectedListener: ItemClickListener
    ): ClusterDropdownData {
        val (selectedClusters, otherClusters) = clusterEntities.partition { it.isSelected }
        val selected = selectedClusters.first()
        return ClusterDropdownData(
            selectedCluster = ClusterPopupItem(selected.id, selected.name),
            otherClusters = otherClusters.map { ClusterPopupItem(it.id, it.name) },
            onSelectedListener
        )
    }
}