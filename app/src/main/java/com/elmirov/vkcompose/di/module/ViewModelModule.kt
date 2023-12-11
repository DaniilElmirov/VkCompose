package com.elmirov.vkcompose.di.module

import androidx.lifecycle.ViewModel
import com.elmirov.vkcompose.di.annotation.ViewModelKey
import com.elmirov.vkcompose.presentation.main.MainViewModel
import com.elmirov.vkcompose.presentation.news.NewsFeedViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(NewsFeedViewModel::class)
    fun bindNewsFeedViewModel(viewModel: NewsFeedViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    fun bindMainViewModel(viewModel: MainViewModel): ViewModel
}