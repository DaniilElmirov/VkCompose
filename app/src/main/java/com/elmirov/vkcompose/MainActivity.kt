package com.elmirov.vkcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.elmirov.vkcompose.ui.theme.MainScreen
import com.elmirov.vkcompose.ui.theme.VkComposeTheme

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            VkComposeTheme {
                MainScreen(viewModel = viewModel)
            }
        }
    }
}