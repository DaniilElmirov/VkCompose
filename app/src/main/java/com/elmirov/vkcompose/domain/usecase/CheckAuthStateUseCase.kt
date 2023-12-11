package com.elmirov.vkcompose.domain.usecase

import com.elmirov.vkcompose.domain.repository.PostRepository

class CheckAuthStateUseCase(
    private val repository: PostRepository,
) {

    suspend operator fun invoke() =
        repository.checkAuthState()
}