package com.elmirov.vkcompose.presentation.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.elmirov.vkcompose.data.repository.PostRepositoryImpl
import com.elmirov.vkcompose.domain.usecase.CheckAuthStateUseCase
import com.elmirov.vkcompose.domain.usecase.GetAuthStateUseCase
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = PostRepositoryImpl(application = application)
    private val getAuthStateUseCase = GetAuthStateUseCase(repository)
    private val checkAuthStateUseCase = CheckAuthStateUseCase(repository)

    val authState = getAuthStateUseCase()

    fun performAuthResult() {
        viewModelScope.launch {
            checkAuthStateUseCase()
        }
    }
}
