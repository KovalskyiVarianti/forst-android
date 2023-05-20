package com.example.forst_android.settings.domain

interface UpdateClusterPrivacyUseCase {
    fun updateClusterPrivacy(clusterId: String, value: Boolean)
}