package com.elmirov.vkcompose.ui.theme

import com.elmirov.vkcompose.domain.Comment
import com.elmirov.vkcompose.domain.FeedPost

sealed interface CommentsScreenState {

    object Initial : CommentsScreenState

    data class Comments(
        val comments: List<Comment>,
        val feedPost: FeedPost,
    ) : CommentsScreenState
}
