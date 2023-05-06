package com.example.forst_android.map.domain

data class MapUserEntity(
    val id: String,
    val name: String,
    val imageUrl: String,
    val isFollowed: Boolean,
    val isEnabled: Boolean
)
