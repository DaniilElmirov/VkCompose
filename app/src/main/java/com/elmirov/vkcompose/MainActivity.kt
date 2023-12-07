package com.elmirov.vkcompose

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.compose.runtime.SideEffect
import com.elmirov.vkcompose.ui.theme.MainScreen
import com.elmirov.vkcompose.ui.theme.VkComposeTheme
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAuthenticationResult
import com.vk.api.sdk.auth.VKScope

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            VkComposeTheme {

                val launcher = rememberLauncherForActivityResult(
                    contract = VK.getVKAuthActivityResultContract(),
                ) {
                    when (it) {
                        is VKAuthenticationResult.Success -> {
                            Log.d("MainActivity", "Регистрация прошла успешно")
                        }

                        is VKAuthenticationResult.Failed -> {
                            Log.d("MainActivity", "Регистрация НЕ прошла")
                        }
                    }
                }

                SideEffect {
                    launcher.launch(listOf(VKScope.WALL))
                }

                MainScreen()
            }
        }
    }
}