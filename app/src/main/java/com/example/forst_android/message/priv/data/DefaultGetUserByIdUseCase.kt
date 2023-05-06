package com.example.forst_android.message.priv.data

import com.example.forst_android.common.data.database.UserRealtimeDatabase
import com.example.forst_android.common.domain.entity.UserEntity
import com.example.forst_android.message.priv.domain.GetUserByIdUseCase
import javax.inject.Inject

class DefaultGetUserByIdUseCase @Inject constructor(
    private val userRealtimeDatabase: UserRealtimeDatabase,
) : GetUserByIdUseCase {
    override suspend fun getUser(id: String): UserEntity {
        return userRealtimeDatabase.getUserById(id).let { user ->
            UserEntity(
                user?.id.orEmpty(),
                user?.name.orEmpty(),
                user?.phoneNumber.orEmpty(),
                user?.photoUri.orEmpty()
            )
        }
    }
}