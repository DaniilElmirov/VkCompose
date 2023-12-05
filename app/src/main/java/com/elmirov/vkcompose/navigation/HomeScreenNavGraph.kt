package com.elmirov.vkcompose.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.elmirov.vkcompose.domain.FeedPost

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
                navArgument(name = Screen.KEY_FEED_POST_ID) {
                    type = NavType.IntType
                }
            ),
        ) { // comments/{feed_post_id}
            val feedPostId = it.arguments?.getInt(Screen.KEY_FEED_POST_ID) ?: 0

            commentsScreenContent(FeedPost(id = feedPostId))
        }
    }
}