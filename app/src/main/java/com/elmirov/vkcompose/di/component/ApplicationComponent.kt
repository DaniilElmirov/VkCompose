package com.elmirov.vkcompose.di.component

import android.content.Context
import com.elmirov.vkcompose.di.annotation.ApplicationScope
import com.elmirov.vkcompose.di.module.DataModule
import com.elmirov.vkcompose.di.module.ViewModelModule
import com.elmirov.vkcompose.presentation.main.MainActivity
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

    fun inject(mainActivity: MainActivity)

    fun getCommentsScreenComponentFactory(): CommentsScreenComponent.Factory

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context,
        ): ApplicationComponent
    }
}