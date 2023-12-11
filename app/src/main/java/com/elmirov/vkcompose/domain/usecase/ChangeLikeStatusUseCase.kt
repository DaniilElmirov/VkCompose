package com.elmirov.vkcompose.domain.usecase

import com.elmirov.vkcompose.domain.entity.FeedPost
import com.elmirov.vkcompose.domain.repository.PostRepository
import javax.inject.Inject

class ChangeLikeStatusUseCase @Inject constructor(
    private val repository: PostRepository,
) {

    suspend operator fun invoke(feedPost: FeedPost) =
        repository.changeLikeStatus(feedPost)
}