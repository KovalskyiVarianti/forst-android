package com.example.forst_android.clusters.di

import com.example.forst_android.clusters.data.ClusterDao
import com.example.forst_android.common.data.database.ClusterDatabase
import com.example.forst_android.common.di.DatabaseModule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(includes = [DatabaseModule::class])
@InstallIn(SingletonComponent::class)
object DaoModule {

    @Provides
    fun clusterDao(clusterDatabase: ClusterDatabase): ClusterDao = clusterDatabase.clustersDao()
}