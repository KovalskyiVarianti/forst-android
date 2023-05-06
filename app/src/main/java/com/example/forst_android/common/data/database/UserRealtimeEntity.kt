package com.example.forst_android.common.data.database

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class UserRealtimeEntity(
    val id: String? = null,
    val name: String? = null,
    val phoneNumber: String? = null,
    val photoUri: String? = null,
)