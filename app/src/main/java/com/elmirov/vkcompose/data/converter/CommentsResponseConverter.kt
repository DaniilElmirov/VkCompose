package com.elmirov.vkcompose.data.converter

import com.elmirov.vkcompose.data.network.model.CommentsResponseModel
import com.elmirov.vkcompose.domain.entity.Comment
import com.elmirov.vkcompose.util.Utils.convertTimestampToDate

class CommentsResponseConverter {

    operator fun invoke(from: CommentsResponseModel): List<Comment> {
        val result = mutableListOf<Comment>()

        val comments = from.content.comments
        val profiles = from.content.profiles

        for (comment in comments) {
            if (comment.text.isBlank()) continue
            val author = profiles.firstOrNull { it.id == comment.authorId } ?: continue

            val postComment = Comment(
                id = comment.id,
                authorName = "${author.firstName} ${author.lastName}",
                authorAvatarUrl = author.avatarUrl,
                text = comment.text,
                publicationDate = comment.date.convertTimestampToDate(),
            )

            result.add(postComment)
        }

        return result
    }
}