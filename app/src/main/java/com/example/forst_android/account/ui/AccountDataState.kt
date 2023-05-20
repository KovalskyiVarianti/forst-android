package com.example.forst_android.account.ui

sealed interface AccountDataState {
    object Loading : AccountDataState
    data class Data(
        val id: String?,
        val phoneNumber: String?,
        val userName: String?,
        val imageUrl: String?,
    ) : AccountDataState
}