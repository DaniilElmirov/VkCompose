package com.elmirov.vkcompose.ui.theme

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.res.stringResource
import com.elmirov.vkcompose.MainViewModel
import com.elmirov.vkcompose.ui.theme.NavigationItem.Favorite
import com.elmirov.vkcompose.ui.theme.NavigationItem.Home
import com.elmirov.vkcompose.ui.theme.NavigationItem.Profile

@Composable
fun MainScreen(
    viewModel: MainViewModel,
) {
    val selectedNavItem by viewModel.selectedNavItem.observeAsState(Home)

    Scaffold(
        bottomBar = {
            NavigationBar {
                val items = listOf(Home, Favorite, Profile)

                items.forEach { item ->
                    NavigationBarItem(
                        selected = selectedNavItem == item,
                        onClick = {
                            viewModel.selectNavItem(item)
                        },
                        icon = {
                            Icon(item.icon, contentDescription = null)
                        },
                        label = {
                            Text(text = stringResource(id = item.titleResId))
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                            selectedTextColor = MaterialTheme.colorScheme.onPrimary,
                            unselectedIconColor = MaterialTheme.colorScheme.onSecondary,
                            unselectedTextColor = MaterialTheme.colorScheme.onSecondary,
                            indicatorColor = MaterialTheme.colorScheme.primary
                        ),
                    )
                }
            }
        }
    ) { paddingValues ->

        when (selectedNavItem) {
            Home -> HomeScreen(viewModel = viewModel, paddingValues = paddingValues)
            Favorite -> Text(text = "Favorite")
            Profile -> Text(text = "Profile")
        }
    }
}