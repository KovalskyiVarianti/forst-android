package com.example.forst_android.settings.data

import com.example.forst_android.clusters.data.realtime.ClusterRealtimeDatabase
import com.example.forst_android.settings.domain.UpdateClusterPrivacyUseCase
import javax.inject.Inject

class DefaultUpdateClusterPrivacyUseCase @Inject constructor(
    private val clusterRealtimeDatabase: ClusterRealtimeDatabase,
) : UpdateClusterPrivacyUseCase {
    override fun updateClusterPrivacy(clusterId: String, value: Boolean) {
        clusterRealtimeDatabase.updateClusterPrivacy(clusterId, value)
    }
}