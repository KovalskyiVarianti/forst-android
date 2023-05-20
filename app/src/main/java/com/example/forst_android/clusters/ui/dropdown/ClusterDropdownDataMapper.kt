package com.example.forst_android.clusters.ui.dropdown

import android.content.Context
import com.example.forst_android.R
import com.example.forst_android.clusters.domain.ClusterEntity
import com.example.forst_android.clusters.ui.dropdown.adapter.ClusterPopupItem
import com.example.forst_android.common.domain.service.UserService
import com.example.forst_android.common.ui.ItemClickListener
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ClusterDropdownDataMapper @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userService: UserService
) {
    fun map(
        clusterEntities: List<ClusterEntity>,
        selectedId: String?,
        onSelectedListener: ItemClickListener
    ): ClusterDropdownData {
        val clusterItems = clusterEntities.map { clusterEntity ->
            ClusterPopupItem.ClusterItem(
                clusterEntity.id,
                clusterEntity.name,
                clusterEntity.ownerId == userService.userUID
            )
        }

        val selected = clusterItems.firstOrNull { it.id == selectedId } ?: kotlin.run {
            clusterItems.first().also { onSelectedListener(it.id) }
        }

        val (createdClusters, joinedClusters) = clusterItems.filterNot { it.id == selectedId }
            .partition { it.isOwner }

        val my = createdClusters.let {
            if (it.isNotEmpty()) {
                listOf(ClusterPopupItem.HeaderItem(context.getString(R.string.my))) + it
            } else {
                it
            }
        }
        val joined = joinedClusters.let {
            if (it.isNotEmpty()) {
                listOf(ClusterPopupItem.HeaderItem(context.getString(R.string.joined))) + it
            } else {
                it
            }
        }

        return ClusterDropdownData(
            selectedCluster = selected,
            otherClusters = my + joined,
            onSelectedListener
        )
    }
}