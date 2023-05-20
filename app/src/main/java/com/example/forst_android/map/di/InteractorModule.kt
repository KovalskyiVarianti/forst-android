package com.example.forst_android.map.di

import com.example.forst_android.map.data.DefaultClusterMapUserListenerInteractor
import com.example.forst_android.map.data.DefaultFollowedUsersListenerInteractor
import com.example.forst_android.map.data.DefaultPointsListenerInteractor
import com.example.forst_android.map.domain.ClusterMapUserListenerInteractor
import com.example.forst_android.map.domain.FollowedUsersListenerInteractor
import com.example.forst_android.map.domain.PointsListenerInteractor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface InteractorModule {

    @Binds
    fun followedUsersListenerInteractor(
        defaultFollowedUsersListenerInteractor: DefaultFollowedUsersListenerInteractor
    ): FollowedUsersListenerInteractor

    @Binds
    fun clusterMapUserListenerInteractor(
        defaultClusterMapUserListenerInteractor: DefaultClusterMapUserListenerInteractor
    ): ClusterMapUserListenerInteractor

    @Binds
    fun pointsListenerInteractor(
        defaultPointsListenerInteractor: DefaultPointsListenerInteractor
    ) : PointsListenerInteractor
}