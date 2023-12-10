package com.elmirov.vkcompose.data.network.model

import com.google.gson.annotations.SerializedName

data class NewsFeedContentModel(
    @SerializedName("items") val posts: List<PostModel>,
    val groups: List<GroupModel>,
    @SerializedName("next_from") val nextFrom: String?,
)
