package com.example.forst_android.common.di

import android.content.Context
import androidx.room.Room
import com.example.forst_android.common.data.database.UserDatabase
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
    fun userDatabase(
        @ApplicationContext context: Context,
    ): UserDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            UserDatabase::class.java,
            "user"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}