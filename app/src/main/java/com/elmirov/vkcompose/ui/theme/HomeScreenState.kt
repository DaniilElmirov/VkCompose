package com.elmirov.vkcompose.ui.theme

import com.elmirov.vkcompose.domain.Comment
import com.elmirov.vkcompose.domain.FeedPost

sealed interface HomeScreenState {

    object Initial : HomeScreenState

    data class Posts(
        val posts: List<FeedPost>,
    ) : HomeScreenState

    data class Comments(
        val feedPost: FeedPost,
        val comments: List<Comment>,
    ) : HomeScreenState
}
