package com.elmirov.vkcompose.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.elmirov.vkcompose.domain.entity.AuthState.Authorized
import com.elmirov.vkcompose.domain.entity.AuthState.Initial
import com.elmirov.vkcompose.domain.entity.AuthState.NoAuthorized
import com.elmirov.vkcompose.getApplicationComponent
import com.elmirov.vkcompose.ui.theme.VkComposeTheme
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKScope

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val component = getApplicationComponent()
            val viewModel: MainViewModel = viewModel(factory = component.getViewModelFactory())
            val authState = viewModel.authState.collectAsState(Initial)

            val launcher = rememberLauncherForActivityResult(
                contract = VK.getVKAuthActivityResultContract(),
            ) {
                viewModel.performAuthResult()
            }

            VkComposeTheme {
                when (authState.value) {
                    Initial -> Unit

                    Authorized -> MainScreen()

                    NoAuthorized -> LoginScreen {
                        launcher.launch(listOf(VKScope.WALL, VKScope.FRIENDS))
                    }
                }
            }
        }
    }
}