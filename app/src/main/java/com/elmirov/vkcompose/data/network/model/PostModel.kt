package com.elmirov.vkcompose.data.network.model

import com.google.gson.annotations.SerializedName

data class PostModel(
    val id: Long,
    @SerializedName("source_id") val communityId: Long,
    val text: String,
    val date: Long,
    val likes: LikesModel,
    val comments: CommentsModel,
    val views: ViewsModel,
    val reposts: RepostsModel,
    val attachments: List<AttachmentModel>?,
)
