package com.example.forst_android.map.ui.adapter

sealed interface MapFollowedItem {
    data class UserMapFollowedItem(
        val id: String,
        val name: String,
        val imageUrl: String,
        val lat: Double?,
        val lng: Double?,
        val lastUpdate: String
    ) : MapFollowedItem

    object AddFollowedUserButton: MapFollowedItem
}
