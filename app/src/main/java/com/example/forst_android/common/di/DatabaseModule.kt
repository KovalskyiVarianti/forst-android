package com.example.forst_android.common.di

import android.content.Context
import androidx.room.Room
import com.example.forst_android.common.data.database.ClusterDatabase
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun clusterDatabase(
        @ApplicationContext context: Context,
    ): ClusterDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            ClusterDatabase::class.java,
            "cluster"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun realtimeDatabase(): FirebaseDatabase {
        return FirebaseDatabase.getInstance(
            "https://forst-de0a8-default-rtdb.europe-west1.firebasedatabase.app"
        )
    }
}