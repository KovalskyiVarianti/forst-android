package com.example.forst_android.auth.domain.entity

sealed interface CodeVerificationResult {
    object Processing : CodeVerificationResult
    object Success : CodeVerificationResult
    data class Failure(val exception: Exception?) : CodeVerificationResult
}