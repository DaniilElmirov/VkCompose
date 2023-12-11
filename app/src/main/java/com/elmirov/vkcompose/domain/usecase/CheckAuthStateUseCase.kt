package com.elmirov.vkcompose.domain.usecase

import com.elmirov.vkcompose.domain.repository.PostRepository
import javax.inject.Inject

class CheckAuthStateUseCase @Inject constructor(
    private val repository: PostRepository,
) {

    suspend operator fun invoke() =
        repository.checkAuthState()
}