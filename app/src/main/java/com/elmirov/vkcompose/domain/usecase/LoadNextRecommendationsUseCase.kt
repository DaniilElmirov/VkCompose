package com.elmirov.vkcompose.domain.usecase

import com.elmirov.vkcompose.domain.repository.PostRepository
import javax.inject.Inject

class LoadNextRecommendationsUseCase @Inject constructor(
    private val repository: PostRepository,
) {

    suspend operator fun invoke() =
        repository.loadNextRecommendations()
}