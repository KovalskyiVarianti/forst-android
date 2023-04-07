package com.example.forst_android.account.data

import com.example.forst_android.account.domain.AccountData
import com.example.forst_android.account.domain.GetAccountDataUseCase
import com.example.forst_android.common.domain.service.UserService
import javax.inject.Inject

class DefaultGetAccountDataUseCase @Inject constructor(
    private val userService: UserService
) : GetAccountDataUseCase {
    override suspend fun getAccountData(): AccountData {
        return AccountData(
            userService.userUID,
            userService.phoneNumber,
        )
    }
}