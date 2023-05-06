package com.example.forst_android.auth.data.service

import android.content.Context
import com.example.forst_android.R
import com.example.forst_android.auth.domain.entity.CodeVerificationResult
import com.example.forst_android.auth.domain.entity.PhoneNumberVerificationResult
import com.example.forst_android.auth.domain.service.AuthService
import com.example.forst_android.common.data.database.UserRealtimeDatabase
import com.example.forst_android.common.data.database.UserRealtimeEntity
import com.example.forst_android.main.ui.MainActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.qualifiers.ActivityContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class FirebaseAuthService @Inject constructor(
    @ActivityContext context: Context,
    private val userRealtimeDatabase: UserRealtimeDatabase,
) : AuthService {

    private val activity = context as MainActivity

    private val phoneNumberVerificationResult =
        MutableStateFlow<PhoneNumberVerificationResult>(PhoneNumberVerificationResult.Processing)

    private val codeVerificationResult =
        MutableStateFlow<CodeVerificationResult>(CodeVerificationResult.Processing)

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
            phoneNumberVerificationResult.value =
                PhoneNumberVerificationResult.Completed(phoneAuthCredential.smsCode)
            Timber.d("onVerificationCompleted: $phoneAuthCredential")
        }

        override fun onVerificationFailed(firebaseException: FirebaseException) {
            phoneNumberVerificationResult.value =
                PhoneNumberVerificationResult.Failed(firebaseException.message.orEmpty())
            Timber.e(firebaseException, "onVerificationFailed")
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            phoneNumberVerificationResult.value =
                PhoneNumberVerificationResult.OnCodeSent(verificationId)
            Timber.d("onCodeSent: $verificationId")
        }
    }

    override fun verifyPhone(phoneNumber: String): StateFlow<PhoneNumberVerificationResult> {
        if (isValidNumber(phoneNumber).not()) {
            phoneNumberVerificationResult.value = PhoneNumberVerificationResult.Failed(
                activity.getString(R.string.phone_format_error_message)
            )
        }
        val options = PhoneAuthOptions.newBuilder(Firebase.auth)
            .setActivity(activity)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
        return phoneNumberVerificationResult.asStateFlow()
    }

    override fun verifyCode(
        verificationId: String,
        code: String
    ): StateFlow<CodeVerificationResult> {
        val credentials = PhoneAuthProvider.getCredential(verificationId, code)
        Firebase.auth.signInWithCredential(credentials).continueWith { task ->
            if (task.isSuccessful) {
                val user = task.result.user
                userRealtimeDatabase.createUser(
                    UserRealtimeEntity(
                        user?.uid,
                        user?.displayName,
                        user?.phoneNumber,
                        user?.photoUrl?.toString()
                    )
                )
            }
        }.addOnCompleteListener { task ->
            codeVerificationResult.value = if (task.isSuccessful) {
                Timber.d("Successful Sign In with verificationId: $verificationId and code: $code")
                CodeVerificationResult.Success
            } else {
                Timber.e(task.exception, "Failed Sign In with code: $code")
                CodeVerificationResult.Failure(task.exception)
            }
        }
        return codeVerificationResult.asStateFlow()
    }

    private fun isValidNumber(phoneNumber: String) =
        phoneNumber.matches("^[+]\\d{10,13}\$".toRegex())
}