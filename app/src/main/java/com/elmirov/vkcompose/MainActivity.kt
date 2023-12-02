package com.elmirov.vkcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.elmirov.vkcompose.ui.theme.PostCard
import com.elmirov.vkcompose.ui.theme.VkComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            VkComposeTheme() {
                Box(
                    modifier = Modifier
                        .background(Color.Gray)
                        .padding(8.dp)
                        .fillMaxSize(),
                ) {
                    PostCard()
                }
            }
        }
    }
}