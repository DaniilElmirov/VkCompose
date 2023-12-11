package com.elmirov.vkcompose.di.component

import com.elmirov.vkcompose.di.module.CommentsViewModelModule
import com.elmirov.vkcompose.domain.entity.FeedPost
import com.elmirov.vkcompose.presentation.ViewModelFactory
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent(
    modules = [
        CommentsViewModelModule::class,
    ]
)
interface CommentsScreenComponent {

    fun getViewModelFactory(): ViewModelFactory

    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance feedPost: FeedPost,
        ): CommentsScreenComponent
    }
}