package com.elmirov.vkcompose.presentation.news

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.elmirov.vkcompose.data.repository.NewsFeedRepository
import com.elmirov.vkcompose.domain.FeedPost
import com.elmirov.vkcompose.presentation.news.NewsFeedScreenState.Initial
import com.elmirov.vkcompose.presentation.news.NewsFeedScreenState.Loading
import com.elmirov.vkcompose.presentation.news.NewsFeedScreenState.Posts
import kotlinx.coroutines.launch

class NewsFeedViewModel(application: Application) : AndroidViewModel(application) {

    private val _screenState = MutableLiveData<NewsFeedScreenState>(Initial)
    val screenState: LiveData<NewsFeedScreenState> = _screenState

    private val repository = NewsFeedRepository(application)

    init {
        _screenState.value = Loading

        loadRecommendations()
    }

    private fun loadRecommendations() {
        viewModelScope.launch {
            val feedPosts = repository.getRecommendations()
            _screenState.value = Posts(posts = feedPosts)
        }
    }

    fun getNextRecommendations() {
        _screenState.value = Posts(
            posts = repository.feedPosts,
            nextPostsIsLoading = true,
        )

        loadRecommendations()
    }

    fun changeLikeStatus(feedPost: FeedPost) {
        viewModelScope.launch {
            repository.changeLikeStatus(feedPost)
            _screenState.value = Posts(posts = repository.feedPosts)
        }
    }

    fun delete(feedPost: FeedPost) {
        viewModelScope.launch {
            repository.deletePost(feedPost)

            _screenState.value = Posts(posts = repository.feedPosts)
        }
    }
}