package com.elmirov.vkcompose.domain.usecase

import com.elmirov.vkcompose.domain.entity.AuthState
import com.elmirov.vkcompose.domain.repository.PostRepository
import kotlinx.coroutines.flow.StateFlow

class GetAuthStateUseCase(
    private val repository: PostRepository,
) {

    operator fun invoke(): StateFlow<AuthState> =
        repository.getAuthState()
}