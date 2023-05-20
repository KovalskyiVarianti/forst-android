package com.example.forst_android.common.domain.service

import com.example.forst_android.common.data.Routes

interface UserService {

    companion object {
        fun getPhotoUrl(userId : String) = "${Routes.BASE_URL}/${Routes.AVATARS}/${userId}"
    }

    val userUID: String?
    val phoneNumber: String?
    val userEmail: String?
    val name: String?
    val photoUrl: String?
    val isLoggedIn: Boolean
    suspend fun updateName(name:String)
    suspend fun signOut()

    suspend fun getIdToken() : String?
}