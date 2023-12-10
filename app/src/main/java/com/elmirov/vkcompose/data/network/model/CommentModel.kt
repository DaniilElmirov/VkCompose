package com.elmirov.vkcompose.data.network.model

import com.google.gson.annotations.SerializedName

data class CommentModel(
    val id: Long,
    @SerializedName("from_id") val authorId: Long,
    val text: String,
    val date: Long,
)
