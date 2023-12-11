package com.elmirov.vkcompose.presentation.comments

import android.app.Application
import androidx.lifecycle.ViewModel
import com.elmirov.vkcompose.data.repository.PostRepository
import com.elmirov.vkcompose.domain.FeedPost
import com.elmirov.vkcompose.presentation.comments.CommentsScreenState.Comments
import kotlinx.coroutines.flow.map

class CommentsViewModel(
    feedPost: FeedPost,
    application: Application,
) : ViewModel() {

    private val repository = PostRepository(application)

    val screenState = repository.getComments(feedPost)
        .map { Comments(comments = it, feedPost = feedPost) }
}