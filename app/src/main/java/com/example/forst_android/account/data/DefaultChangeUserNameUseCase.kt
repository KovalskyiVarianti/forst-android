package com.example.forst_android.account.data

import com.example.forst_android.account.domain.ChangeNameResult
import com.example.forst_android.account.domain.ChangeUserNameUseCase
import com.example.forst_android.common.data.database.UserRealtimeDatabase
import com.example.forst_android.common.domain.service.UserService
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

class DefaultChangeUserNameUseCase @Inject constructor(
    private val userService: UserService,
    private val userRealtimeDatabase: UserRealtimeDatabase,
) : ChangeUserNameUseCase {
    override suspend fun changeName(name: String): ChangeNameResult {
        userService.updateName(name)
        return suspendCancellableCoroutine {
            userRealtimeDatabase.updateName(userService.userUID.orEmpty(), name)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        it.resume(ChangeNameResult.Success)
                    } else {
                        it.resume(ChangeNameResult.Failure)
                    }
                }
        }
    }
}