package com.elmirov.vkcompose.presentation.news

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.elmirov.vkcompose.R
import com.elmirov.vkcompose.domain.FeedPost
import com.elmirov.vkcompose.domain.StatisticItem
import com.elmirov.vkcompose.domain.StatisticType
import com.elmirov.vkcompose.ui.theme.DarkRed

@Composable
fun PostCard(
    modifier: Modifier = Modifier,
    feedPost: FeedPost,
    onLikeClickListener: (StatisticItem) -> Unit,
    onCommentClickListener: (StatisticItem) -> Unit,
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primary),
        elevation = CardDefaults.cardElevation(8.dp),
    ) {
        Header(
            feedPost = feedPost,
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            text = feedPost.contentText,
        )

        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            model = feedPost.contentImageUrl,
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
        )

        Statistic(
            statistics = feedPost.statistics,
            onLikeClickListener = onLikeClickListener,
            onCommentClickListener = onCommentClickListener,
            isFavourite = feedPost.isLiked,
        )
    }
}

@Composable
private fun Header(
    feedPost: FeedPost,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImage(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape),
            model = feedPost.communityImageUrl,
            contentDescription = null,
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(
            modifier = Modifier
                .weight(1f),
        ) {
            Text(
                text = feedPost.communityName,
                color = MaterialTheme.colorScheme.onPrimary,
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = feedPost.publicationDate,
                color = MaterialTheme.colorScheme.onSecondary,
            )
        }

        Icon(
            imageVector = Icons.Rounded.MoreVert,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSecondary
        )
    }
}

@Composable
private fun Statistic(
    statistics: List<StatisticItem>,
    onLikeClickListener: (StatisticItem) -> Unit,
    onCommentClickListener: (StatisticItem) -> Unit,
    isFavourite: Boolean,
) {
    Row(
        modifier = Modifier.padding(8.dp),
    ) {
        Row(
            modifier = Modifier.weight(1f),
        ) {
            val viewsItem = statistics.getItemByType(StatisticType.VIEWS)

            IconWithText(
                iconResId = R.drawable.ic_views_count,
                text = formatStatisticCount(viewsItem.count),
            )
        }

        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            val sharesItem = statistics.getItemByType(StatisticType.SHARES)
            IconWithText(
                iconResId = R.drawable.ic_share,
                text = formatStatisticCount(sharesItem.count),
            )

            val commentsItem = statistics.getItemByType(StatisticType.COMMENTS)
            IconWithText(
                iconResId = R.drawable.ic_comment,
                text = formatStatisticCount(commentsItem.count),
                onItemClickListener = {
                    onCommentClickListener(commentsItem)
                },
            )

            val likesItem = statistics.getItemByType(StatisticType.LIKES)
            IconWithText(
                iconResId = if (isFavourite) R.drawable.ic_like_set else R.drawable.ic_like,
                text = formatStatisticCount(likesItem.count),
                onItemClickListener = {
                    onLikeClickListener(likesItem)
                },
                tint = if (isFavourite) DarkRed else MaterialTheme.colorScheme.onSecondary,
            )
        }
    }
}

private fun formatStatisticCount(count: Int): String {
    return if (count > 100_000) {
        String.format("%sK", (count / 1000))
    } else if (count > 1000) {
        String.format("%.1fK", (count / 1000f))
    } else {
        count.toString()
    }
}

private fun List<StatisticItem>.getItemByType(type: StatisticType): StatisticItem {
    return this.find { it.type == type }
        ?: throw IllegalStateException("Require StatisticType not exist")
}

@Composable
private fun IconWithText(
    iconResId: Int,
    text: String,
    onItemClickListener: (() -> Unit)? = null,
    tint: Color = MaterialTheme.colorScheme.onSecondary,
) {
    val modifier = if (onItemClickListener == null)
        Modifier
    else {
        Modifier.clickable {
            onItemClickListener()
        }
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier.size(20.dp),
            painter = painterResource(id = iconResId),
            contentDescription = null,
            tint = tint,
        )

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = text,
            color = MaterialTheme.colorScheme.onSecondary,
        )
    }
}