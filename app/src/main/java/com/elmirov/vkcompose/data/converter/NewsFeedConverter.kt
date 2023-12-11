package com.elmirov.vkcompose.data.converter

import com.elmirov.vkcompose.data.network.model.NewsFeedResponseModel
import com.elmirov.vkcompose.domain.entity.FeedPost
import com.elmirov.vkcompose.domain.entity.StatisticItem
import com.elmirov.vkcompose.domain.entity.StatisticType.COMMENTS
import com.elmirov.vkcompose.domain.entity.StatisticType.LIKES
import com.elmirov.vkcompose.domain.entity.StatisticType.SHARES
import com.elmirov.vkcompose.domain.entity.StatisticType.VIEWS
import com.elmirov.vkcompose.util.Utils.convertTimestampToDate
import kotlin.math.absoluteValue

class NewsFeedConverter {
    operator fun invoke(from: NewsFeedResponseModel): List<FeedPost> {
        val result = mutableListOf<FeedPost>()

        val posts = from.newsFeedContent.posts
        val groups = from.newsFeedContent.groups

        for (post in posts) {
            val group = groups.find { it.id == post.communityId.absoluteValue } ?: continue
            val feedPost = FeedPost(
                id = post.id,
                communityId = post.communityId,
                communityName = group.name,
                publicationDate = post.date.convertTimestampToDate(),
                communityImageUrl = group.imageUrl,
                contentText = post.text,
                contentImageUrl = post.attachments?.firstOrNull()?.photo?.photoUrls?.lastOrNull()?.url,
                statistics = listOf(
                    StatisticItem(type = LIKES, count = post.likes.count),
                    StatisticItem(type = VIEWS, count = post.views.count),
                    StatisticItem(type = SHARES, count = post.reposts.count),
                    StatisticItem(type = COMMENTS, count = post.comments.count),
                ),
                isLiked = post.likes.userLikes > 0,
            )

            result.add(feedPost)
        }

        return result
    }
}