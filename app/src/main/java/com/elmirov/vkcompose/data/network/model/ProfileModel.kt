package com.elmirov.vkcompose.data.network.model

import com.google.gson.annotations.SerializedName

data class ProfileModel(
    val id: Long,
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String,
    @SerializedName("photo_100") val avatarUrl: String,
)
