package com.elmirov.vkcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.elmirov.vkcompose.ui.theme.MainScreen
import com.elmirov.vkcompose.ui.theme.VkComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            VkComposeTheme {
                MainScreen()
            }
        }
    }
}