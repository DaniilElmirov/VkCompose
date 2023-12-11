package com.elmirov.vkcompose.domain.usecase

import com.elmirov.vkcompose.domain.entity.AuthState
import com.elmirov.vkcompose.domain.repository.PostRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetAuthStateUseCase @Inject constructor(
    private val repository: PostRepository,
) {

    operator fun invoke(): StateFlow<AuthState> =
        repository.getAuthState()
}