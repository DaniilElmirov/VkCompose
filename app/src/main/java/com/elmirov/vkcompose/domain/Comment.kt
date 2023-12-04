package com.elmirov.vkcompose.domain

import com.elmirov.vkcompose.R

data class Comment(
    val id: Int,
    val authorName: String = "Author Name",
    val authorAvatarId: Int = R.drawable.comment_author_avatar,
    val text: String = "Comment text",
    val publicationData: String = "14:00",
)
