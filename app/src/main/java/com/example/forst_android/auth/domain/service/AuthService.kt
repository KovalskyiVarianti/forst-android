package com.example.forst_android.auth.domain.service

import com.example.forst_android.auth.domain.entity.CodeVerificationResult
import com.example.forst_android.auth.domain.entity.PhoneNumberVerificationResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface AuthService {
    fun verifyPhone(phoneNumber: String): Flow<PhoneNumberVerificationResult>
    fun verifyCode(verificationId: String, code: String): StateFlow<CodeVerificationResult>
}