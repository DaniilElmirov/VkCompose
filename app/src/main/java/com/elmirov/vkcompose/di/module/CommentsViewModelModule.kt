package com.elmirov.vkcompose.di.module

import androidx.lifecycle.ViewModel
import com.elmirov.vkcompose.di.annotation.ViewModelKey
import com.elmirov.vkcompose.presentation.comments.CommentsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface CommentsViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(CommentsViewModel::class)
    fun bindCommentsViewModel(viewModel: CommentsViewModel): ViewModel
}