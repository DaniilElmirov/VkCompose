package com.elmirov.vkcompose.data.network.api

import com.elmirov.vkcompose.data.network.model.NewsFeedResponseModel
import retrofit2.http.GET
import retrofit2.http.Query

interface VkApi {

    @GET("newsfeed.getRecommended?v=5.131")
    suspend fun getRecommendation(
        @Query("access_token") token: String,
    ): NewsFeedResponseModel

    @GET("likes.add?v=5.131&type=post")
    suspend fun addLike(
        @Query("access_token") token: String,
        @Query("owner_id") ownerId: Long,
        @Query("item_id") postId: Long,
    )
}