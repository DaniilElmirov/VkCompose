package com.elmirov.vkcompose.presentation.news

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.elmirov.vkcompose.data.repository.PostRepositoryImpl
import com.elmirov.vkcompose.domain.entity.FeedPost
import com.elmirov.vkcompose.domain.usecase.ChangeLikeStatusUseCase
import com.elmirov.vkcompose.domain.usecase.DeletePostUseCase
import com.elmirov.vkcompose.domain.usecase.GetRecommendationsUseCase
import com.elmirov.vkcompose.domain.usecase.LoadNextRecommendationsUseCase
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

    private val repository = PostRepositoryImpl(application)
    private val getRecommendationsUseCase = GetRecommendationsUseCase(repository)
    private val loadNextRecommendationsUseCase = LoadNextRecommendationsUseCase(repository)
    private val changeLikeStatusUseCase = ChangeLikeStatusUseCase(repository)
    private val deletePostUseCase = DeletePostUseCase(repository)

    private val exceptionHandler = CoroutineExceptionHandler { _, _ ->
        Log.d("exceptionHandler", "exception")
    }

    private val recommendations = getRecommendationsUseCase()

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
            loadNextRecommendationsUseCase()
        }
    }

    fun changeLikeStatus(feedPost: FeedPost) {
        viewModelScope.launch(exceptionHandler) {
            changeLikeStatusUseCase(feedPost)
        }
    }

    fun delete(feedPost: FeedPost) {
        viewModelScope.launch(exceptionHandler) {
            deletePostUseCase(feedPost)
        }
    }
}