package com.example.forst_android.clusters.di

import com.example.forst_android.common.data.database.ClusterDao
import com.example.forst_android.common.data.database.UserDatabase
import com.example.forst_android.common.di.DatabaseModule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(includes = [DatabaseModule::class])
@InstallIn(SingletonComponent::class)
object DaoModule {

    @Provides
    fun clusterDao(userDatabase: UserDatabase): ClusterDao = userDatabase.clustersDao()
}