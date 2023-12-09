package com.elmirov.vkcompose.data.network.model

import com.google.gson.annotations.SerializedName

data class LikesModel(
    val count: Int,
    @SerializedName("user_likes") val userLikes: Int,
)
