package com.elmirov.vkcompose.ui.theme

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.elmirov.vkcompose.MainViewModel
import com.elmirov.vkcompose.domain.FeedPost
import com.elmirov.vkcompose.ui.theme.HomeScreenState.Comments
import com.elmirov.vkcompose.ui.theme.HomeScreenState.Initial
import com.elmirov.vkcompose.ui.theme.HomeScreenState.Posts

@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    paddingValues: PaddingValues,
) {
    val screenState = viewModel.screenState.observeAsState(Initial)

    when (val currentState = screenState.value) {
        is Posts -> FeedPosts(
            viewModel = viewModel,
            paddingValues = paddingValues,
            posts = currentState.posts,
        )

        is Comments -> {
            CommentsScreen(
                feedPost = currentState.feedPost,
                comments = currentState.comments,
                onBackPressed = {
                    viewModel.closeComments()
                },
            )

            BackHandler {
                viewModel.closeComments()
            }
        }

        Initial -> Unit
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun FeedPosts(
    viewModel: MainViewModel,
    paddingValues: PaddingValues,
    posts: List<FeedPost>,
) {
    LazyColumn(
        modifier = Modifier.padding(paddingValues),
        contentPadding = PaddingValues(
            top = 16.dp,
            start = 8.dp,
            end = 8.dp,
            bottom = 88.dp,
        ),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(
            items = posts,
            key = { it.id }
        ) { feedPost ->

            val dismissState = rememberDismissState()

            if (dismissState.isDismissed(DismissDirection.EndToStart))
                viewModel.delete(feedPost)

            SwipeToDismiss(
                modifier = Modifier.animateItemPlacement(),
                state = dismissState,
                directions = setOf(DismissDirection.EndToStart),
                background = {},
                dismissContent = {
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
                        onCommentClickListener = {
                            viewModel.showComments(feedPost = feedPost)
                        },
                    )
                },
            )
        }
    }
}