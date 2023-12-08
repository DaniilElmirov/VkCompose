package com.elmirov.vkcompose.data.network.model

import com.google.gson.annotations.SerializedName

data class PhotoModel(
    @SerializedName("sizes") val photoUrls: List<PhotoUrlModel>,
)
