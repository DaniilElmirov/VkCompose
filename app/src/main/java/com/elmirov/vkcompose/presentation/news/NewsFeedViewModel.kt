package com.elmirov.vkcompose.presentation.news

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.elmirov.vkcompose.data.repository.PostRepository
import com.elmirov.vkcompose.domain.entity.FeedPost
import com.elmirov.vkcompose.presentation.news.NewsFeedScreenState.Loading
import com.elmirov.vkcompose.presentation.news.NewsFeedScreenState.Posts
import com.elmirov.vkcompose.util.Utils.mergeWith
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class NewsFeedViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = PostRepository(application)

    private val exceptionHandler = CoroutineExceptionHandler { _, _ ->
        Log.d("exceptionHandler", "exception")
    }

    private val recommendations = repository.recommendations

    private val loadNextPostsEvent = MutableSharedFlow<Unit>()
    private val loadNextPosts = flow {
        loadNextPostsEvent.collect {
            emit(
                Posts(
                    posts = recommendations.value,
                    nextPostsIsLoading = true,
                )
            )
        }
    }

    val screenState = recommendations
        .filter { it.isNotEmpty() }
        .map { Posts(posts = it) as NewsFeedScreenState }
        .onStart { emit(Loading) }
        .mergeWith(loadNextPosts)

    fun getNextRecommendations() {
        viewModelScope.launch {
            loadNextPostsEvent.emit(Unit)
            repository.loadNextPosts()
        }
    }

    fun changeLikeStatus(feedPost: FeedPost) {
        viewModelScope.launch(exceptionHandler) {
            repository.changeLikeStatus(feedPost)
        }
    }

    fun delete(feedPost: FeedPost) {
        viewModelScope.launch(exceptionHandler) {
            repository.deletePost(feedPost)
        }
    }
}