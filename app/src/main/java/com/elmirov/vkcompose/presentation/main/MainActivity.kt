package com.elmirov.vkcompose.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.elmirov.vkcompose.VkComposeApplication
import com.elmirov.vkcompose.domain.entity.AuthState.Authorized
import com.elmirov.vkcompose.domain.entity.AuthState.Initial
import com.elmirov.vkcompose.domain.entity.AuthState.NoAuthorized
import com.elmirov.vkcompose.presentation.ViewModelFactory
import com.elmirov.vkcompose.ui.theme.VkComposeTheme
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKScope
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val component by lazy {
        (application as VkComposeApplication).component
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)

        setContent {
            VkComposeTheme {

                val viewModel: MainViewModel = viewModel(factory = viewModelFactory)
                val authState = viewModel.authState.collectAsState(Initial)

                val launcher = rememberLauncherForActivityResult(
                    contract = VK.getVKAuthActivityResultContract(),
                ) {
                    viewModel.performAuthResult()
                }

                when (authState.value) {
                    Initial -> Unit

                    Authorized -> MainScreen(
                        viewModelFactory = viewModelFactory,
                    )

                    NoAuthorized -> LoginScreen {
                        launcher.launch(listOf(VKScope.WALL, VKScope.FRIENDS))
                    }
                }
            }
        }
    }
}