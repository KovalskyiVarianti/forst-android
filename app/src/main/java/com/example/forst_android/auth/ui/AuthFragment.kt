package com.example.forst_android.auth.ui

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.forst_android.R
import com.example.forst_android.auth.domain.entity.CodeVerificationResult
import com.example.forst_android.auth.domain.entity.PhoneNumberVerificationResult
import com.example.forst_android.auth.domain.service.AuthService
import com.example.forst_android.common.ui.hide
import com.example.forst_android.common.ui.show
import com.example.forst_android.common.ui.viewBinding
import com.example.forst_android.databinding.FragmentAuthBinding
import com.example.forst_android.main.navigation.NavigationManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AuthFragment : Fragment(R.layout.fragment_auth) {

    private val binding: FragmentAuthBinding by viewBinding()
    private val authViewModel: AuthViewModel by viewModels()

    @Inject
    lateinit var navigationManager: NavigationManager

    @Inject
    lateinit var authService: AuthService

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.phoneLoginButton.setOnClickListener {
            val phoneNumber = "+${binding.phoneNumberInput.text}"
            if (phoneNumber.isNotBlank()) {
                lifecycleScope.launch {
                    authService.verifyPhone(phoneNumber).collect { result ->
                        processPhoneVerificationResult(result)
                    }
                }
            }
        }

        binding.codeVerifyButton.setOnClickListener {
            val code = binding.codeInput.text.toString()
            if (code.isNotBlank()) {
                lifecycleScope.launch {
                    authViewModel.verificationId.collect { verificationId ->
                        verificationId?.let {
                            authService.verifyCode(verificationId, code).collect { result ->
                                processCodeVerificationResult(result)
                            }
                        }
                    }
                }
            }
        }

        binding.codeInput.addTextChangedListener { binding.codeLayout.error = null }
        binding.phoneNumberInput.addTextChangedListener { binding.phoneNumberLayout.error = null }
    }

    private fun processPhoneVerificationResult(result: PhoneNumberVerificationResult) {

        fun onVerificationStarted() {
            with(binding) {
                phoneNumberLayout.isEnabled = false
                phoneLoginButton.hide()
                progressBar.show()
            }
        }

        fun onVerificationFailed(result: PhoneNumberVerificationResult.Failed) {
            with(binding) {
                phoneNumberLayout.isEnabled = true
                phoneNumberLayout.error = result.errorText
                phoneLoginButton.show()
                progressBar.hide()
            }
        }

        fun onVerificationCompleted(result: PhoneNumberVerificationResult.Completed) {
            with(binding) {
                codeInput.setText(result.code.orEmpty())
                progressBar.hide()
            }
        }

        fun onCodeSent(verificationId: String) {
            with(binding) {
                codeLayout.show()
                codeLayout.isEnabled = true
                codeVerifyButton.show()
                phoneNumberLayout.isEnabled = false
                phoneNumberLayout.error = null
                phoneLoginButton.hide()
                progressBar.hide()
            }
            authViewModel.onCodeSent(verificationId)
        }

        when (result) {
            is PhoneNumberVerificationResult.OnCodeSent -> onCodeSent(result.verificationId)
            is PhoneNumberVerificationResult.Completed -> onVerificationCompleted(result)
            is PhoneNumberVerificationResult.Failed -> onVerificationFailed(result)
            PhoneNumberVerificationResult.Processing -> onVerificationStarted()
        }
    }

    private fun processCodeVerificationResult(result: CodeVerificationResult) {
        fun onVerificationFailed(result: CodeVerificationResult.Failure) {
            with(binding) {
                codeLayout.isEnabled = false
                codeLayout.error = result.exception?.message
                codeInput.setText("")
                phoneNumberLayout.isEnabled = true
                phoneLoginButton.show()
                codeVerifyButton.hide()
                progressBar.hide()
            }
        }

        fun onVerificationStarted() {
            with(binding) {
                codeLayout.isEnabled = false
                codeVerifyButton.hide()
                progressBar.show()
            }
        }

        fun onVerificationSuccess() {
            navigationManager.navigate(
                lifecycleScope,
                AuthFragmentDirections.actionAuthFragmentToMainFragment()
            )
        }

        when (result) {
            is CodeVerificationResult.Failure -> onVerificationFailed(result)
            CodeVerificationResult.Processing -> onVerificationStarted()
            CodeVerificationResult.Success -> onVerificationSuccess()
        }
    }
}