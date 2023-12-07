package com.elmirov.vkcompose.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.elmirov.vkcompose.presentation.main.AuthState.Authorized
import com.elmirov.vkcompose.presentation.main.AuthState.Initial
import com.elmirov.vkcompose.presentation.main.AuthState.NoAuthorized
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAuthenticationResult

class MainViewModel : ViewModel() {

    private val _authState = MutableLiveData<AuthState>(Initial)
    val authState: LiveData<AuthState> = _authState

    init {
        _authState.value = if (VK.isLoggedIn()) Authorized else NoAuthorized
    }

    fun performAuthResult(
        result: VKAuthenticationResult,
    ) {
        when (result) {
            is VKAuthenticationResult.Success -> {
                _authState.value = Authorized
            }

            is VKAuthenticationResult.Failed -> {
                _authState.value = NoAuthorized
            }
        }
    }
}
