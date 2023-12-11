package com.elmirov.vkcompose.domain.usecase

import com.elmirov.vkcompose.domain.entity.FeedPost
import com.elmirov.vkcompose.domain.repository.PostRepository

class DeletePostUseCase(
    private val repository: PostRepository,
) {

    suspend operator fun invoke(feedPost: FeedPost) =
        repository.deletePost(feedPost)
}