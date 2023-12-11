package com.elmirov.vkcompose.presentation.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.elmirov.vkcompose.data.repository.PostRepository
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = PostRepository(application = application)

    val authState = repository.authState

    fun performAuthResult() {
        viewModelScope.launch {
            repository.checkAuthState()
        }
    }
}
