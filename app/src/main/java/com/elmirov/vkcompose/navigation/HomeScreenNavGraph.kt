package com.elmirov.vkcompose.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.elmirov.vkcompose.domain.FeedPost
import com.google.gson.Gson

fun NavGraphBuilder.homeScreenNavGraph(
    newsFeedScreenContent: @Composable () -> Unit,
    commentsScreenContent: @Composable (FeedPost) -> Unit,
) {
    navigation(
        startDestination = Screen.NewsFeed.route,
        route = Screen.Home.route,
    ) {
        composable(
            route = Screen.NewsFeed.route,
        ) {
            newsFeedScreenContent()
        }

        composable(
            route = Screen.Comments.route,
            arguments = listOf(
                navArgument(name = Screen.KEY_FEED_POST) {
                    type = NavType.StringType
                }
            ),
        ) { // comments/{feed_post_id}
            val feedPostJson = it.arguments?.getString(Screen.KEY_FEED_POST) ?: ""
            val feedPost = Gson().fromJson(feedPostJson, FeedPost::class.java)

            commentsScreenContent(feedPost)
        }
    }
}