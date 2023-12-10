package com.elmirov.vkcompose.data.network.model

import com.google.gson.annotations.SerializedName

data class CommentsResponseModel(
    @SerializedName("response") val content: CommentsContentModel,
)
