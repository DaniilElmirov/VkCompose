package com.elmirov.vkcompose

import android.app.Application
import com.elmirov.vkcompose.di.component.ApplicationComponent
import com.elmirov.vkcompose.di.component.DaggerApplicationComponent

class VkComposeApplication : Application() {

    val component: ApplicationComponent by lazy {
        DaggerApplicationComponent.factory().create(this)
    }
}