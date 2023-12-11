package com.elmirov.vkcompose.domain.usecase

import com.elmirov.vkcompose.domain.entity.Comment
import com.elmirov.vkcompose.domain.entity.FeedPost
import com.elmirov.vkcompose.domain.repository.PostRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetCommentsUseCase @Inject constructor(
    private val repository: PostRepository,
) {

    operator fun invoke(feedPost: FeedPost): StateFlow<List<Comment>> =
        repository.getComments(feedPost)
}