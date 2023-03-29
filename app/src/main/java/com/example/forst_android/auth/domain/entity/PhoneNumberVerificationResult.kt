package com.example.forst_android.auth.domain.entity

sealed interface PhoneNumberVerificationResult {
    object Processing: PhoneNumberVerificationResult
    data class Completed(val code: String?) : PhoneNumberVerificationResult
    data class Failed(val errorText: String) : PhoneNumberVerificationResult
    data class OnCodeSent(val verificationId: String) : PhoneNumberVerificationResult
}