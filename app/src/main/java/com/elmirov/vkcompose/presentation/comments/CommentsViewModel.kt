package com.elmirov.vkcompose.presentation.comments

import androidx.lifecycle.ViewModel
import com.elmirov.vkcompose.domain.entity.FeedPost
import com.elmirov.vkcompose.domain.usecase.GetCommentsUseCase
import com.elmirov.vkcompose.presentation.comments.CommentsScreenState.Comments
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CommentsViewModel @Inject constructor(
    private val feedPost: FeedPost,
    private val getCommentsUseCase: GetCommentsUseCase,
) : ViewModel() {

    val screenState = getCommentsUseCase(feedPost)
        .map { Comments(comments = it, feedPost = feedPost) }
}