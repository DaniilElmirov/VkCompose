package com.elmirov.vkcompose.presentation.news

import com.elmirov.vkcompose.domain.entity.FeedPost

sealed interface NewsFeedScreenState {

    object Initial : NewsFeedScreenState

    object Loading : NewsFeedScreenState

    data class Posts(
        val posts: List<FeedPost>,
        val nextPostsIsLoading: Boolean = false,
    ) : NewsFeedScreenState
}
