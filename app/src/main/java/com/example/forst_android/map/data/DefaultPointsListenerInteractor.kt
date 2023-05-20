package com.example.forst_android.map.data

import com.example.forst_android.common.data.database.UserRealtimeDatabase
import com.example.forst_android.map.domain.PointEntity
import com.example.forst_android.map.domain.PointsListenerInteractor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DefaultPointsListenerInteractor @Inject constructor(
    private val pointRealtimeDatabase: PointRealtimeDatabase,
    private val userRealtimeDatabase: UserRealtimeDatabase,
) : PointsListenerInteractor{
    override fun addPointsListener(clusterId: String): Flow<List<PointEntity>> {
        return pointRealtimeDatabase.addPointsListener(clusterId).map { points ->
            points.map { point ->
                PointEntity(
                    point.id.orEmpty(),
                    point.name.orEmpty(),
                    point.lat!!,
                    point.lng!!,
                    userRealtimeDatabase.getUserById(point.creatorId.orEmpty())?.name.orEmpty(),
                    point.createTime!!
                )
            }
        }
    }

    override fun removePointsListener() {
        pointRealtimeDatabase.removePointsListener()
    }
}