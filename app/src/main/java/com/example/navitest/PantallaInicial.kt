package com.example.navitest

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.navitest.pages.HomePage
import com.example.navitest.pages.NotificationsPage
import com.example.navitest.pages.SettingsPage

@Composable
fun PantallaInicial() {

    val navItemLis = listOf(
        ItemsNav("Uno", Icons.Default.Home),
        ItemsNav("Dos", Icons.Default.Settings),
        ItemsNav("Tres", Icons.Default.Notifications),
    )

    var selectedIndex by remember {
        mutableIntStateOf(0)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                navItemLis.forEachIndexed { index, nav ->
                    NavigationBarItem(
                        selected = selectedIndex == index,
                        onClick = { selectedIndex = index },
                        icon = { Icon(imageVector = nav.icon, contentDescription = "Hola") },
                        label = { Text(nav.label)},
                    )
                }
            }
        }
    )
    { innerPadding ->
        ContenidoPantalla(modifier = Modifier.padding(innerPadding),selectedIndex)
    }
}

@Composable
fun ContenidoPantalla(modifier: Modifier = Modifier, selectedIndex: Int) {
    when(selectedIndex){
        0 -> HomePage()
        1 -> SettingsPage()
        2 -> NotificationsPage()
    }
}