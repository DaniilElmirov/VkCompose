package com.elmirov.vkcompose.data.repository

import android.app.Application
import com.elmirov.vkcompose.data.converter.ResponseConverter
import com.elmirov.vkcompose.data.network.api.ApiFactory
import com.elmirov.vkcompose.domain.FeedPost
import com.elmirov.vkcompose.domain.StatisticItem
import com.elmirov.vkcompose.domain.StatisticType
import com.vk.api.sdk.VKPreferencesKeyValueStorage
import com.vk.api.sdk.auth.VKAccessToken

class NewsFeedRepository(
    application: Application,
) {

    private val storage = VKPreferencesKeyValueStorage(application)
    private val token = VKAccessToken.restore(storage)

    private val apiService = ApiFactory.apiService
    private val converter = ResponseConverter()

    private val _feedPosts = mutableListOf<FeedPost>()
    val feedPosts: List<FeedPost>
        get() = _feedPosts.toList()

    suspend fun getRecommendations(): List<FeedPost> {
        val response = apiService.getRecommendation(getToken())

        val posts = converter(response)
        _feedPosts.addAll(posts)

        return posts
    }

    suspend fun addLike(feedPost: FeedPost) {
        val response = apiService.addLike(
            token = getToken(),
            ownerId = feedPost.communityId,
            postId = feedPost.id,
        )

        val newLikesCount = response.likes.count
        val newStatistics = feedPost.statistics.toMutableList().apply {
            removeIf { it.type == StatisticType.LIKES }
            add(StatisticItem(type = StatisticType.LIKES, count = newLikesCount))
        }

        val newPost = feedPost.copy(statistics = newStatistics, isLiked = true)
        val postIndex = _feedPosts.indexOf(feedPost)
        _feedPosts[postIndex] = newPost
    }


    private fun getToken(): String = token?.accessToken ?: throw IllegalStateException("null TOKEN")
}