package com.elmirov.vkcompose.data.repository

import android.app.Application
import com.elmirov.vkcompose.data.converter.CommentsResponseConverter
import com.elmirov.vkcompose.data.converter.NewsFeedConverter
import com.elmirov.vkcompose.data.network.api.ApiFactory
import com.elmirov.vkcompose.domain.Comment
import com.elmirov.vkcompose.domain.FeedPost
import com.elmirov.vkcompose.domain.StatisticItem
import com.elmirov.vkcompose.domain.StatisticType
import com.elmirov.vkcompose.util.Utils.mergeWith
import com.vk.api.sdk.VKPreferencesKeyValueStorage
import com.vk.api.sdk.auth.VKAccessToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.stateIn

class PostRepository(
    application: Application,
) {

    companion object {
        private const val RETRY_TIMEOUT_MILLIS = 3000L
    }

    private val storage = VKPreferencesKeyValueStorage(application)
    private val token = VKAccessToken.restore(storage)

    private val apiService = ApiFactory.apiService
    private val newsFeedConverter = NewsFeedConverter()
    private val commentsResponseConverter = CommentsResponseConverter()

    private val _feedPosts = mutableListOf<FeedPost>()
    private val feedPosts: List<FeedPost>
        get() = _feedPosts.toList()

    private var nextFrom: String? = null

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    private val nextPostsNeededEvent = MutableSharedFlow<Unit>(replay = 1)
    private val refreshPosts = MutableSharedFlow<List<FeedPost>>()

    private val loadedPosts = flow {
        nextPostsNeededEvent.emit(Unit)

        nextPostsNeededEvent.collect {
            val startFrom = nextFrom
            if (startFrom == null && feedPosts.isNotEmpty()) {
                emit(feedPosts)
                return@collect
            }

            val response = if (startFrom == null)
                apiService.getRecommendation(getToken())
            else
                apiService.getRecommendation(getToken(), startFrom)
            nextFrom = response.newsFeedContent.nextFrom

            val posts = newsFeedConverter(response)
            _feedPosts.addAll(posts)

            emit(feedPosts)
        }
    }.retry {
        delay(RETRY_TIMEOUT_MILLIS)
        true
    }

    val recommendations: StateFlow<List<FeedPost>> = loadedPosts
        .mergeWith(refreshPosts)
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.Lazily,
            initialValue = feedPosts,
        )

    suspend fun loadNextPosts() {
        nextPostsNeededEvent.emit(Unit)
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

        refreshPosts.emit(feedPosts)
    }

    suspend fun deletePost(feedPost: FeedPost) {
        apiService.ignorePost(
            token = getToken(),
            ownerId = feedPost.communityId,
            postId = feedPost.id,
        )

        _feedPosts.remove(feedPost)
        refreshPosts.emit(feedPosts)
    }

    fun getComments(feedPost: FeedPost): Flow<List<Comment>> = flow {
        val comments = apiService.getComments(
            token = getToken(),
            ownerId = feedPost.communityId,
            postId = feedPost.id,
        )

        emit(commentsResponseConverter(comments))
    }.retry {
        delay(RETRY_TIMEOUT_MILLIS)
        true
    }

    private fun getToken(): String = token?.accessToken ?: throw IllegalStateException("null TOKEN")
}