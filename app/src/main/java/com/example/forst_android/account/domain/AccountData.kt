package com.example.forst_android.account.domain

import android.net.Uri

data class AccountData(
    val id: String?,
    val phoneNumber: String?,
    val userName: String?,
    val imageUrl: Uri?,
)
