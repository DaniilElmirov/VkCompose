package com.elmirov.vkcompose.data.repository

import com.elmirov.vkcompose.data.converter.CommentsResponseConverter
import com.elmirov.vkcompose.data.converter.NewsFeedConverter
import com.elmirov.vkcompose.data.network.api.VkApi
import com.elmirov.vkcompose.domain.entity.AuthState
import com.elmirov.vkcompose.domain.entity.Comment
import com.elmirov.vkcompose.domain.entity.FeedPost
import com.elmirov.vkcompose.domain.entity.StatisticItem
import com.elmirov.vkcompose.domain.entity.StatisticType
import com.elmirov.vkcompose.domain.repository.PostRepository
import com.elmirov.vkcompose.util.Utils.mergeWith
import com.vk.api.sdk.VKPreferencesKeyValueStorage
import com.vk.api.sdk.auth.VKAccessToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val storage: VKPreferencesKeyValueStorage,
    private val api: VkApi,
    private val newsFeedConverter: NewsFeedConverter,
    private val commentsResponseConverter: CommentsResponseConverter,
) : PostRepository {
    companion object {
        private const val RETRY_TIMEOUT_MILLIS = 3000L
    }

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    private val token
        get() = VKAccessToken.restore(storage)

    private val checkAuthEvent = MutableSharedFlow<Unit>(replay = 1)

    private val _feedPosts = mutableListOf<FeedPost>()
    private val feedPosts: List<FeedPost>
        get() = _feedPosts.toList()

    private var nextFrom: String? = null

    private val nextPostsNeededEvent = MutableSharedFlow<Unit>(replay = 1)
    private val refreshPosts = MutableSharedFlow<List<FeedPost>>()

    private val authState = flow { //Временное решение
        checkAuthEvent.emit(Unit)

        checkAuthEvent.collect {
            val currentToken = token
            val loggedIn = currentToken != null && currentToken.isValid
            val authState = if (loggedIn) AuthState.Authorized else AuthState.NoAuthorized
            emit(authState)
        }
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.Lazily,
        initialValue = AuthState.Initial
    )

    override suspend fun checkAuthState() {
        checkAuthEvent.emit(Unit)
    }

    override fun getAuthState(): StateFlow<AuthState> = authState

    private val loadedPosts = flow {
        nextPostsNeededEvent.emit(Unit)

        nextPostsNeededEvent.collect {
            val startFrom = nextFrom
            if (startFrom == null && feedPosts.isNotEmpty()) {
                emit(feedPosts)
                return@collect
            }

            val response = if (startFrom == null)
                api.getRecommendation(getToken())
            else
                api.getRecommendation(getToken(), startFrom)
            nextFrom = response.newsFeedContent.nextFrom

            val posts = newsFeedConverter(response)
            _feedPosts.addAll(posts)

            emit(feedPosts)
        }
    }.retry {
        delay(RETRY_TIMEOUT_MILLIS)
        true
    }

    private val recommendations: StateFlow<List<FeedPost>> =
        loadedPosts
            .mergeWith(refreshPosts)
            .stateIn(
                scope = coroutineScope,
                started = SharingStarted.Lazily,
                initialValue = feedPosts,
            )

    override fun getRecommendations(): StateFlow<List<FeedPost>> = recommendations

    override suspend fun loadNextRecommendations() {
        nextPostsNeededEvent.emit(Unit)
    }

    override suspend fun changeLikeStatus(feedPost: FeedPost) {
        val response = if (feedPost.isLiked) {
            api.deleteLike(
                token = getToken(),
                ownerId = feedPost.communityId,
                postId = feedPost.id,
            )
        } else {
            api.addLike(
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

    override suspend fun deletePost(feedPost: FeedPost) {
        api.ignorePost(
            token = getToken(),
            ownerId = feedPost.communityId,
            postId = feedPost.id,
        )

        _feedPosts.remove(feedPost)
        refreshPosts.emit(feedPosts)
    }

    override fun getComments(feedPost: FeedPost): StateFlow<List<Comment>> = flow {
        val comments = api.getComments(
            token = getToken(),
            ownerId = feedPost.communityId,
            postId = feedPost.id,
        )

        emit(commentsResponseConverter(comments))
    }.retry {
        delay(RETRY_TIMEOUT_MILLIS)
        true
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.Lazily,
        initialValue = listOf(),
    )

    private fun getToken(): String = token?.accessToken ?: throw IllegalStateException("null TOKEN")
}