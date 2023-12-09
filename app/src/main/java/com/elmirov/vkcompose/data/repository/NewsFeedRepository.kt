package com.elmirov.vkcompose.data.repository

import android.app.Application
import com.elmirov.vkcompose.data.converter.ResponseConverter
import com.elmirov.vkcompose.data.network.api.ApiFactory
import com.elmirov.vkcompose.domain.FeedPost
import com.vk.api.sdk.VKPreferencesKeyValueStorage
import com.vk.api.sdk.auth.VKAccessToken
import java.lang.IllegalStateException

class NewsFeedRepository(
    application: Application,
) {

    private val storage = VKPreferencesKeyValueStorage(application)
    private val token = VKAccessToken.restore(storage)

    private val apiService = ApiFactory.apiService
    private val converter = ResponseConverter()

    suspend fun getRecommendations(): List<FeedPost> {
        val response = apiService.getRecommendation(getToken())

        return converter(response)
    }

    private fun getToken(): String = token?.accessToken ?: throw IllegalStateException("null TOKEN")
}