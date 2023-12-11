package com.elmirov.vkcompose.domain.usecase

import com.elmirov.vkcompose.domain.entity.FeedPost
import com.elmirov.vkcompose.domain.repository.PostRepository
import kotlinx.coroutines.flow.StateFlow

class GetRecommendationsUseCase(
    private val repository: PostRepository,
) {

    operator fun invoke(): StateFlow<List<FeedPost>> =
        repository.getRecommendations()
}