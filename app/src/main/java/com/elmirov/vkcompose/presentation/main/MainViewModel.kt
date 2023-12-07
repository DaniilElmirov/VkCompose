package com.elmirov.vkcompose.presentation.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.elmirov.vkcompose.presentation.main.AuthState.Authorized
import com.elmirov.vkcompose.presentation.main.AuthState.Initial
import com.elmirov.vkcompose.presentation.main.AuthState.NoAuthorized
import com.vk.api.sdk.VKPreferencesKeyValueStorage
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthenticationResult

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val _authState = MutableLiveData<AuthState>(Initial)
    val authState: LiveData<AuthState> = _authState

    init {
        val storage = VKPreferencesKeyValueStorage(application)
        val token = VKAccessToken.restore(storage)

        val loggedIn = token != null && token.isValid
        _authState.value = if (loggedIn) Authorized else NoAuthorized
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
