package com.elmirov.vkcompose.data.network.model

import com.google.gson.annotations.SerializedName

data class CommentsContentModel(
    @SerializedName("items") val comments: List<CommentModel>,
    val profiles: List<ProfileModel>,
)
