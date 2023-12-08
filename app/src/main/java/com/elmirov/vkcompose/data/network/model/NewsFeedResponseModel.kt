package com.elmirov.vkcompose.data.network.model

import com.google.gson.annotations.SerializedName

data class NewsFeedResponseModel(
    @SerializedName("response") val newsFeedContent: NewsFeedContentModel,
)
