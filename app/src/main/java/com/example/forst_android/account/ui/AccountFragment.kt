package com.example.forst_android.account.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.forst_android.R
import com.example.forst_android.common.ui.viewBinding
import com.example.forst_android.databinding.FragmentAccountBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AccountFragment : Fragment(R.layout.fragment_account) {

    private val binding: FragmentAccountBinding by viewBinding()

    private val accountViewModel: AccountViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        accountViewModel.fetchAccountData()
        lifecycleScope.launch {
            accountViewModel.getAccountData()
                .flowWithLifecycle(lifecycle, Lifecycle.State.CREATED)
                .collect(::processAccountDataState)
        }
        binding.changeNameButton.setOnClickListener {
            val newUserName = binding.userNameInput.text.toString().takeIf { it.isNotBlank() }
            accountViewModel.changeUserName(newUserName)
            binding.userNameInput.setText("")
        }

    }

    private fun processAccountDataState(accountDataState: AccountDataState) {
        when(accountDataState) {
            is AccountDataState.Data -> {
                binding.apply {
                    accountPhoneNumber.text = accountDataState.phoneNumber.orEmpty()
                    userNameInput.hint = accountDataState.userName ?: getString(R.string.user_name_empty)
                }
            }
            AccountDataState.Loading -> {
                // nothing
            }
        }
    }
}