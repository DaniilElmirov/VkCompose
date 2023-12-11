package com.elmirov.vkcompose.domain.repository

import com.elmirov.vkcompose.domain.entity.AuthState
import com.elmirov.vkcompose.domain.entity.Comment
import com.elmirov.vkcompose.domain.entity.FeedPost
import kotlinx.coroutines.flow.StateFlow

interface PostRepository {

    fun getAuthState(): StateFlow<AuthState>

    fun getRecommendations(): StateFlow<List<FeedPost>>

    fun getComments(feedPost: FeedPost): StateFlow<List<Comment>>

    suspend fun checkAuthState()

    suspend fun loadNextRecommendations()

    suspend fun deletePost(feedPost: FeedPost)

    suspend fun changeLikeStatus(feedPost: FeedPost)
}