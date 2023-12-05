package com.elmirov.vkcompose.ui.theme

import com.elmirov.vkcompose.domain.FeedPost

sealed interface NewsFeedScreenState {

    object Initial : NewsFeedScreenState

    data class Posts(
        val posts: List<FeedPost>,
    ) : NewsFeedScreenState
}
