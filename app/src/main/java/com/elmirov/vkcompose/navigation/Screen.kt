package com.elmirov.vkcompose.navigation

import android.net.Uri
import com.elmirov.vkcompose.domain.FeedPost

sealed class Screen(
    val route: String,
) {
    companion object {
        const val KEY_FEED_POST_ID = "feed_post_id"

        private const val ROUTE_HOME = "home"
        private const val ROUTE_NEWS_FEED = "news_feed"
        private const val ROUTE_COMMENTS = "comments/{$KEY_FEED_POST_ID}"
        private const val ROUTE_FAVOURITE = "favourite"
        private const val ROUTE_PROFILE = "profile"
    }

    object Home : Screen(ROUTE_HOME)

    object NewsFeed : Screen(ROUTE_NEWS_FEED)

    object Comments : Screen(ROUTE_COMMENTS) {

        private const val ROUTE_FOR_ARGS = "comments"
        fun getRouteWithArgs(feedPost: FeedPost): String = "$ROUTE_FOR_ARGS/${feedPost.id}"
    }

    object Favourite : Screen(ROUTE_FAVOURITE)

    object Profile : Screen(ROUTE_PROFILE)
}

fun String.encode(): String = Uri.encode(this) //Для экранирования спец символов в строке
// (прим: при передаче строки как параметра)