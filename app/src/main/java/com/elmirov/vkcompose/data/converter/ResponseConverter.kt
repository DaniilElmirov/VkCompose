package com.elmirov.vkcompose.data.converter

import com.elmirov.vkcompose.data.network.model.NewsFeedResponseModel
import com.elmirov.vkcompose.domain.FeedPost
import com.elmirov.vkcompose.domain.StatisticItem
import com.elmirov.vkcompose.domain.StatisticType.COMMENTS
import com.elmirov.vkcompose.domain.StatisticType.LIKES
import com.elmirov.vkcompose.domain.StatisticType.SHARES
import com.elmirov.vkcompose.domain.StatisticType.VIEWS
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.absoluteValue

class ResponseConverter {

    companion object {
        private const val IN_MILLIS = 1000
    }

    operator fun invoke(from: NewsFeedResponseModel): List<FeedPost> {
        val result = mutableListOf<FeedPost>()

        val posts = from.newsFeedContent.posts
        val groups = from.newsFeedContent.groups

        for (post in posts) {
            val group = groups.find { it.id == post.communityId.absoluteValue } ?: continue
            val feedPost = FeedPost(
                id = post.id,
                communityName = group.name,
                publicationDate = convertTimestampToDate(post.date * IN_MILLIS),
                communityImageUrl = group.imageUrl,
                contentText = post.text,
                contentImageUrl = post.attachments?.firstOrNull()?.photo?.photoUrls?.lastOrNull()?.url,
                statistics = listOf(
                    StatisticItem(type = LIKES, count = post.likes.count),
                    StatisticItem(type = VIEWS, count = post.views.count),
                    StatisticItem(type = SHARES, count = post.reposts.count),
                    StatisticItem(type = COMMENTS, count = post.comments.count),
                ),
                isFavourite = post.isFavourite
            )

            result.add(feedPost)
        }

        return result
    }

    private fun convertTimestampToDate(timestamp: Long): String {
        val date = Date(timestamp)
        return SimpleDateFormat("d MMMM yyyy, hh:mm", Locale.getDefault()).format(date)
    }
}