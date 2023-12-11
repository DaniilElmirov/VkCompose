package com.elmirov.vkcompose.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.elmirov.vkcompose.domain.entity.FeedPost

@Composable
fun AppNavGraph(
    navHostController: NavHostController,
    newsFeedScreenContent: @Composable () -> Unit,
    commentsScreenContent: @Composable (FeedPost) -> Unit,
    favouriteScreenContent: @Composable () -> Unit,
    profileScreenContent: @Composable () -> Unit,
) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.Home.route,
    ) {

        homeScreenNavGraph(
            newsFeedScreenContent = newsFeedScreenContent,
            commentsScreenContent = commentsScreenContent,
        )

        composable(
            route = Screen.Favourite.route,
        ) {
            favouriteScreenContent()
        }

        composable(
            route = Screen.Profile.route,
        ) {
            profileScreenContent()
        }
    }
}