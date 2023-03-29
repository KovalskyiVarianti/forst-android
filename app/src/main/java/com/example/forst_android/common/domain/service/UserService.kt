package com.example.forst_android.common.domain.service

import android.net.Uri

interface UserService {
    val userUID: String?
    val userEmail: String?
    val name: String?
    val photoUrl: Uri?
    val isLoggedIn: Boolean
    suspend fun updateName(name:String)
    suspend fun updatePhoto(uri: Uri?)
    suspend fun signOut()
}