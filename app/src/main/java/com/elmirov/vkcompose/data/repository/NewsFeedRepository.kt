package com.elmirov.vkcompose.data.repository

import android.app.Application
import com.elmirov.vkcompose.data.converter.NewsFeedConverter
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
    private val converter = NewsFeedConverter()

    private val _feedPosts = mutableListOf<FeedPost>()
    val feedPosts: List<FeedPost>
        get() = _feedPosts.toList()

    private var nextFrom: String? = null

    suspend fun getRecommendations(): List<FeedPost> {
        val startFrom = nextFrom
        if (startFrom == null && feedPosts.isNotEmpty()) return feedPosts

        val response = if (startFrom == null)
            apiService.getRecommendation(getToken())
        else
            apiService.getRecommendation(getToken(), startFrom)
        nextFrom = response.newsFeedContent.nextFrom

        val posts = converter(response)
        _feedPosts.addAll(posts)

        return feedPosts
    }

    suspend fun changeLikeStatus(feedPost: FeedPost) {
        val response = if (feedPost.isLiked) {
            apiService.deleteLike(
                token = getToken(),
                ownerId = feedPost.communityId,
                postId = feedPost.id,
            )
        } else {
            apiService.addLike(
                token = getToken(),
                ownerId = feedPost.communityId,
                postId = feedPost.id,
            )
        }

        val newLikesCount = response.likes.count
        val newStatistics = feedPost.statistics.toMutableList().apply {
            removeIf { it.type == StatisticType.LIKES }
            add(StatisticItem(type = StatisticType.LIKES, count = newLikesCount))
        }

        val newPost = feedPost.copy(statistics = newStatistics, isLiked = !feedPost.isLiked)
        val postIndex = _feedPosts.indexOf(feedPost)
        _feedPosts[postIndex] = newPost
    }

    suspend fun deletePost(feedPost: FeedPost) {
        apiService.ignorePost(
            token = getToken(),
            ownerId = feedPost.communityId,
            postId = feedPost.id,
        )

        _feedPosts.remove(feedPost)
    }

    private fun getToken(): String = token?.accessToken ?: throw IllegalStateException("null TOKEN")
}