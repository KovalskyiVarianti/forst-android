package com.example.forst_android.map.ui

import com.example.forst_android.map.domain.FollowedUserEntity
import com.example.forst_android.map.ui.adapter.MapFollowedItem
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class MapFollowedItemMapper @Inject constructor() {

    private val formatter by lazy { SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()) }

    fun map(followedUserEntities: List<FollowedUserEntity>): List<MapFollowedItem> {
        return listOf(MapFollowedItem.AddFollowedUserButton) + followedUserEntities.map { user ->
            MapFollowedItem.UserMapFollowedItem(
                user.id,
                user.name,
                user.imageUrl,
                user.location?.lat,
                user.location?.lng,
                formatter.format(user.lastUpdate)
            )
        }
    }
}