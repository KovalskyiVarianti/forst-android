package com.example.forst_android.clusters.ui.dropdown

import com.example.forst_android.clusters.domain.ClusterEntity
import com.example.forst_android.common.ui.ItemClickListener
import javax.inject.Inject

class ClusterDropdownDataMapper @Inject constructor() {
    fun map(
        clusterEntities: List<ClusterEntity>,
        selectedId: String?,
        onSelectedListener: ItemClickListener
    ): ClusterDropdownData {
        val selected = clusterEntities.firstOrNull { it.id == selectedId } ?: kotlin.run {
            clusterEntities.first().also { onSelectedListener(it.id) }
        }
        val otherClusters = clusterEntities.filterNot { it.id == selectedId }
        return ClusterDropdownData(
            selectedCluster = ClusterPopupItem(selected.id, selected.name),
            otherClusters = otherClusters.map { ClusterPopupItem(it.id, it.name) },
            onSelectedListener
        )
    }
}