package com.elmirov.vkcompose.ui.theme

import androidx.compose.foundation.clickable
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.currentBackStackEntryAsState
import com.elmirov.vkcompose.domain.FeedPost
import com.elmirov.vkcompose.navigation.AppNavGraph
import com.elmirov.vkcompose.navigation.rememberNavigationState
import com.elmirov.vkcompose.ui.theme.NavigationItem.Favorite
import com.elmirov.vkcompose.ui.theme.NavigationItem.Home
import com.elmirov.vkcompose.ui.theme.NavigationItem.Profile

@Composable
fun MainScreen() {
    val navigationState = rememberNavigationState()

    val commentsToPost: MutableState<FeedPost?> = remember { //Временное решение
        mutableStateOf(null)
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                val items = listOf(Home, Favorite, Profile)

                val navBackStackEntry by navigationState.navHostController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                items.forEach { item ->
                    NavigationBarItem(
                        selected = currentRoute == item.screen.route,
                        onClick = { navigationState.navigateTo(item.screen.route) },
                        icon = {
                            Icon(item.icon, contentDescription = null)
                        },
                        label = {
                            Text(text = stringResource(id = item.titleResId))
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                            selectedTextColor = MaterialTheme.colorScheme.onPrimary,
                            unselectedIconColor = MaterialTheme.colorScheme.onSecondary,
                            unselectedTextColor = MaterialTheme.colorScheme.onSecondary,
                            indicatorColor = MaterialTheme.colorScheme.primary
                        ),
                    )
                }
            }
        }
    ) { paddingValues ->

        AppNavGraph(
            navHostController = navigationState.navHostController,
            homeScreenContent = {
                //Временное решение
                if (commentsToPost.value == null) {
                    NewsFeedScreen(
                        paddingValues = paddingValues,
                        onCommentClickListener = {
                            commentsToPost.value = it
                        },
                    )
                } else
                    CommentsScreen(
                        feedPost = commentsToPost.value!!,
                        onBackPressed = {
                            commentsToPost.value = null
                        },
                    )
            },
            favouriteScreenContent = { TextCounter(name = "Favourite") },
            profileScreenContent = { TextCounter(name = "Profile") },
        )
    }
}

@Composable
private fun TextCounter(name: String) {
    var count by rememberSaveable {
        mutableIntStateOf(0)
    }

    Text(
        modifier = Modifier.clickable { count++ },
        text = "$name Count: $count",
    )
}