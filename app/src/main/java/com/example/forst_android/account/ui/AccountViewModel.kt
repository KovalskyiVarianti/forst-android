package com.example.forst_android.account.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.forst_android.account.domain.ChangeNameResult
import com.example.forst_android.account.domain.ChangeUserNameUseCase
import com.example.forst_android.account.domain.GetAccountDataUseCase
import com.example.forst_android.common.coroutines.CoroutineDispatchers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val coroutineDispatchers: CoroutineDispatchers,
    private val getAccountDataUseCase: GetAccountDataUseCase,
    private val changeUserNameUseCase: ChangeUserNameUseCase,
) : ViewModel() {

    private val accountData = MutableStateFlow<AccountDataState>(AccountDataState.Loading)
    fun getAccountData(): StateFlow<AccountDataState> = accountData.asStateFlow()

    fun fetchAccountData() = viewModelScope.launch(coroutineDispatchers.io) {
        with(getAccountDataUseCase.getAccountData()) {
            accountData.emit(
                AccountDataState.Data(id, phoneNumber, userName, imageUrl)
            )
        }
    }

    fun changeUserName(userName: String?) {
        userName?.let {
            viewModelScope.launch(coroutineDispatchers.io) {
                when(changeUserNameUseCase.changeName(userName)) {
                    ChangeNameResult.Success -> {
                        fetchAccountData()
                    }
                    ChangeNameResult.Failure -> {
                        // nothing
                    }
                }
            }
        }
    }

}