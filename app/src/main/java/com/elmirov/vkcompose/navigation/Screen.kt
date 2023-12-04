package com.elmirov.vkcompose.navigation

sealed class Screen(
    val route: String,
) {
    private companion object {
        const val ROUTE_NEWS_FEED = "news_feed"
        const val ROUTE_FAVOURITE = "favourite"
        const val ROUTE_PROFILE = "profile"
    }

    object NewsFeed: Screen(ROUTE_NEWS_FEED)

    object Favourite: Screen(ROUTE_FAVOURITE)

    object Profile: Screen(ROUTE_PROFILE)
}
