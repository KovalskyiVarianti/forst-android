package com.example.forst_android.common.data.service

import android.net.Uri
import com.example.forst_android.common.domain.service.UserService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

class FirebaseUserService @Inject constructor() : UserService {

    private val firebaseAuth: FirebaseAuth = Firebase.auth

    override val userUID
        get() = firebaseAuth.uid

    override val userEmail
        get() = firebaseAuth.currentUser?.email

    override val name
        get() = firebaseAuth.currentUser?.displayName

    override val photoUrl
        get() = firebaseAuth.currentUser?.photoUrl

    override val isLoggedIn
        get() = firebaseAuth.currentUser != null

    override suspend fun updatePhoto(uri: Uri?) {
        firebaseAuth.currentUser?.updateProfile(
            UserProfileChangeRequest.Builder()
                .setPhotoUri(uri)
                .build()
        )
    }

    override suspend fun signOut() {
        firebaseAuth.signOut()
    }

    override suspend fun updateName(name: String) {
        firebaseAuth.currentUser?.updateProfile(
            UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build()
        )
    }
}