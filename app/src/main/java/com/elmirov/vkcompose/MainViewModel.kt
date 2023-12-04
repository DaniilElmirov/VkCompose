package com.elmirov.vkcompose

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.elmirov.vkcompose.domain.Comment
import com.elmirov.vkcompose.domain.FeedPost
import com.elmirov.vkcompose.domain.StatisticItem
import com.elmirov.vkcompose.ui.theme.HomeScreenState
import com.elmirov.vkcompose.ui.theme.HomeScreenState.*

class MainViewModel : ViewModel() {

    private val comments = mutableListOf<Comment>().apply {
        repeat(20) {
            add(Comment(id = it))
        }
    }

    private val initialList = mutableListOf<FeedPost>().apply {
        repeat(20) {
            add(FeedPost(id = it))
        }
    }
    private val initialState = Posts(posts = initialList)

    private val _screenState = MutableLiveData<HomeScreenState>(initialState)
    val screenState: LiveData<HomeScreenState> = _screenState

    private var savedState: HomeScreenState? = initialState

    fun showComments(
        feedPost: FeedPost,
    ) {
        savedState = _screenState.value
        _screenState.value = Comments(feedPost = feedPost, comments = comments)
    }

    fun closeComments() {
        _screenState.value = savedState
    }

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