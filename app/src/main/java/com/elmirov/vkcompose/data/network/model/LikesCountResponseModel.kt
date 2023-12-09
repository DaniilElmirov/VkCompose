package com.elmirov.vkcompose.data.network.model

import com.google.gson.annotations.SerializedName

data class LikesCountResponseModel(
    @SerializedName("response") val likes: LikesCountModel,
)