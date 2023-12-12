package com.elmirov.vkcompose

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.elmirov.vkcompose.di.component.ApplicationComponent
import com.elmirov.vkcompose.di.component.DaggerApplicationComponent

class VkComposeApplication : Application() {

    val component: ApplicationComponent by lazy {
        DaggerApplicationComponent.factory().create(this)
    }
}

@Composable
fun getApplicationComponent(): ApplicationComponent =
    (LocalContext.current.applicationContext as VkComposeApplication).component