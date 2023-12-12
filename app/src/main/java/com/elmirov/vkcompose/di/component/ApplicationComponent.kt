package com.elmirov.vkcompose.di.component

import android.content.Context
import com.elmirov.vkcompose.di.annotation.ApplicationScope
import com.elmirov.vkcompose.di.module.DataModule
import com.elmirov.vkcompose.di.module.ViewModelModule
import com.elmirov.vkcompose.presentation.ViewModelFactory
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(
    modules = [
        DataModule::class,
        ViewModelModule::class,
    ]
)
interface ApplicationComponent {

    fun getViewModelFactory(): ViewModelFactory

    fun getCommentsScreenComponentFactory(): CommentsScreenComponent.Factory

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context,
        ): ApplicationComponent
    }
}