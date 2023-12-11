package com.elmirov.vkcompose.presentation.comments

import android.app.Application
import androidx.lifecycle.ViewModel
import com.elmirov.vkcompose.data.repository.PostRepositoryImpl
import com.elmirov.vkcompose.domain.entity.FeedPost
import com.elmirov.vkcompose.domain.usecase.GetCommentsUseCase
import com.elmirov.vkcompose.presentation.comments.CommentsScreenState.Comments
import kotlinx.coroutines.flow.map

class CommentsViewModel(
    feedPost: FeedPost,
    application: Application,
) : ViewModel() {

    private val repository = PostRepositoryImpl(application)
    private val getCommentsUseCase = GetCommentsUseCase(repository)

    val screenState = getCommentsUseCase(feedPost)
        .map { Comments(comments = it, feedPost = feedPost) }
}