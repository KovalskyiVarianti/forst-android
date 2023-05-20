package com.example.forst_android.message.group.domain

import java.io.InputStream

interface PhotoGroupSendUseCase {
    suspend fun sendPhoto(
        clusterId: String,
        groupId: String,
        userId: String,
        groupMembers: List<String>,
        inputStream: InputStream,
    )
}