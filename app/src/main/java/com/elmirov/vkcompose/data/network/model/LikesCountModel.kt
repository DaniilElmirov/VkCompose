package com.elmirov.vkcompose.data.network.model

import com.google.gson.annotations.SerializedName

data class LikesCountModel(
    @SerializedName("likes") val count: Int,
)
