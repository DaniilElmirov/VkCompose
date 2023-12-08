package com.elmirov.vkcompose.data.network.model

import com.google.gson.annotations.SerializedName

data class GroupModel(
    val id: Long,
    val name: String,
    @SerializedName("photo_200") val imageUrl: String
)
