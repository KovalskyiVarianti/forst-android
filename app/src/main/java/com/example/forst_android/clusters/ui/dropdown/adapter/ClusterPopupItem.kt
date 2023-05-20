package com.example.forst_android.clusters.ui.dropdown.adapter


sealed interface ClusterPopupItem {
    data class ClusterItem(
        val id: String,
        val name: String,
        val isOwner: Boolean
    ) : ClusterPopupItem

    data class HeaderItem(val text: String) : ClusterPopupItem
}
