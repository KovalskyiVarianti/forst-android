package com.example.forst_android.clusters.join.ui.adapter

data class ClusterJoinItem(val id: String, val name: String, val type: ClusterJoinType) {
    enum class ClusterJoinType {
        SELF, JOINED, NOT_JOINED
    }
}