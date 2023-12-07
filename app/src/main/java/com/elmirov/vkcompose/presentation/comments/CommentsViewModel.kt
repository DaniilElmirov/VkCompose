package com.elmirov.vkcompose.presentation.comments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.elmirov.vkcompose.domain.Comment
import com.elmirov.vkcompose.domain.FeedPost
import com.elmirov.vkcompose.presentation.comments.CommentsScreenState.Comments
import com.elmirov.vkcompose.presentation.comments.CommentsScreenState.Initial

class CommentsViewModel(
    feedPost: FeedPost,
) : ViewModel() {

    private val _screenState = MutableLiveData<CommentsScreenState>(Initial)
    val screenState: LiveData<CommentsScreenState> = _screenState

    init {
        loadComments(feedPost)
    }

    private fun loadComments(feedPost: FeedPost) {
        val comments = mutableListOf<Comment>().apply {
            repeat(20) {
                add(Comment(id = it))
            }
        }

        _screenState.value = Comments(comments = comments, feedPost = feedPost)
    }
}