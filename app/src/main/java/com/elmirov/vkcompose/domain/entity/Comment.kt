package com.elmirov.vkcompose.domain.entity

data class Comment(
    val id: Long,
    val authorName: String,
    val authorAvatarUrl: String,
    val text: String,
    val publicationDate: String,
)
