package com.example.forst_android.clusters.data.realtime

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class ClusterRealtimeEntity(
    val id: String? = null,
    val name: String? = null,
    val ownerId: String? = null,
    val private: Boolean? = null,
)
