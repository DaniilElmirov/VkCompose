package com.elmirov.vkcompose.ui.theme

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.elmirov.vkcompose.MainViewModel
import com.elmirov.vkcompose.ui.theme.NavigationItem.Favorite
import com.elmirov.vkcompose.ui.theme.NavigationItem.Home
import com.elmirov.vkcompose.ui.theme.NavigationItem.Profile

@Composable
fun MainScreen(
    viewModel: MainViewModel,
) {

    Scaffold(
        bottomBar = {
            NavigationBar {
                val items = listOf(Home, Favorite, Profile)
                val selectedItemPosition = rememberSaveable {
                    mutableStateOf(0)
                }

                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = index == selectedItemPosition.value,
                        onClick = { selectedItemPosition.value = index },
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
    ) { padding ->
        Log.d("TAG", "$padding")

        val feedPosts = viewModel.feedPosts.observeAsState(listOf())

        LazyColumn(
            contentPadding = PaddingValues(
                top = 16.dp,
                start = 8.dp,
                end = 8.dp,
                bottom = 88.dp,
            ),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(
                items = feedPosts.value,
                key = { it.id }
            ) { feedPost ->
                PostCard(
                    feedPost = feedPost,
                    onLikeClickListener = { statisticItem ->
                        viewModel.updateCount(feedPost = feedPost, item = statisticItem)
                    },
                    onShareClickListener = { statisticItem ->
                        viewModel.updateCount(feedPost = feedPost, item = statisticItem)
                    },
                    onViewsClickListener = { statisticItem ->
                        viewModel.updateCount(feedPost = feedPost, item = statisticItem)
                    },
                    onCommentClickListener = { statisticItem ->
                        viewModel.updateCount(feedPost = feedPost, item = statisticItem)
                    },
                )
            }
        }
    }
}