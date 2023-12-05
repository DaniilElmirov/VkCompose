package com.elmirov.vkcompose

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.elmirov.vkcompose.domain.FeedPost
import com.elmirov.vkcompose.domain.StatisticItem
import com.elmirov.vkcompose.ui.theme.NewsFeedScreenState
import com.elmirov.vkcompose.ui.theme.NewsFeedScreenState.Posts

class NewsFeedViewModel : ViewModel() {

    private val initialList = mutableListOf<FeedPost>().apply {
        repeat(20) {
            add(FeedPost(id = it))
        }
    }
    private val initialState = Posts(posts = initialList)

    private val _screenState = MutableLiveData<NewsFeedScreenState>(initialState)
    val screenState: LiveData<NewsFeedScreenState> = _screenState

    fun updateCount(feedPost: FeedPost, item: StatisticItem) {
        val currentState = screenState.value

        if (currentState !is Posts)
            return

        val oldPosts = currentState.posts.toMutableList()
        val oldStatistics = feedPost.statistics

        val newStatistics = oldStatistics.toMutableList().apply {
            replaceAll { oldItem ->
                if (oldItem.type == item.type)
                    oldItem.copy(count = oldItem.count + 1)
                else
                    oldItem
            }
        }

        val newFeedPost = feedPost.copy(statistics = newStatistics)
        val newPosts = oldPosts.apply {
            replaceAll {
                if (it.id == newFeedPost.id)
                    newFeedPost
                else
                    it
            }
        }

        _screenState.value = Posts(posts = newPosts)
    }

    fun delete(feedPost: FeedPost) {
        val currentState = screenState.value

        if (currentState !is Posts)
            return

        val oldPosts = currentState.posts.toMutableList()
        oldPosts.remove(feedPost)

        _screenState.value = Posts(posts = oldPosts)
    }
}