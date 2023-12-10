package com.elmirov.vkcompose.presentation.comments

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elmirov.vkcompose.data.repository.PostRepository
import com.elmirov.vkcompose.domain.FeedPost
import com.elmirov.vkcompose.presentation.comments.CommentsScreenState.Comments
import com.elmirov.vkcompose.presentation.comments.CommentsScreenState.Initial
import kotlinx.coroutines.launch

class CommentsViewModel(
    feedPost: FeedPost,
    application: Application,
) : ViewModel() {

    private val repository = PostRepository(application)

    private val _screenState = MutableLiveData<CommentsScreenState>(Initial)
    val screenState: LiveData<CommentsScreenState> = _screenState

    init {
        getComments(feedPost)
    }

    private fun getComments(feedPost: FeedPost) {
        viewModelScope.launch {
            val comments = repository.getComments(feedPost)
            _screenState.value = Comments(comments = comments, feedPost = feedPost)
        }
    }
}