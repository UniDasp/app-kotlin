
package com.example.navitest

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.navitest.pages.HomePage
import com.example.navitest.pages.NotificationsPage
import com.example.navitest.pages.SearchPage
import com.example.navitest.pages.SettingsPage

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun PantallaInicial() {

    val navItemLis = listOf(
        ItemsNav("Home", Icons.Default.Home),
        ItemsNav("Productos", Icons.Default.ShoppingCart),
        ItemsNav("Notificaciones", Icons.Default.Notifications),
        ItemsNav("Cuenta", Icons.Default.Person),
    )

    var selectedIndex by remember {
        mutableIntStateOf(0)
    }

    var showCartDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Levelup Gamer") },
                actions = {
                    IconButton(onClick = { showCartDialog = true }) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                navItemLis.forEachIndexed { index, nav ->
                    NavigationBarItem(
                        selected = selectedIndex == index,
                        onClick = { selectedIndex = index },
                        icon = { Icon(imageVector = nav.icon, contentDescription = nav.label) },
                        label = { Text(nav.label)},
                    )
                }
            }
        }
    )
    { innerPadding ->
        ContenidoPantalla(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            selectedIndex = selectedIndex
        )
    }

    if (showCartDialog) {
        AlertDialog(
            onDismissRequest = { showCartDialog = false },
            title = { Text("Carrito") },
            text = {
                com.example.navitest.pages.CartPage(modifier = Modifier.fillMaxSize())
            },
            confirmButton = {
                TextButton(onClick = { showCartDialog = false }) { Text("Cerrar") }
            }
        )
    }
}

@Composable
fun ContenidoPantalla(modifier: Modifier = Modifier, selectedIndex: Int) {
    when(selectedIndex){
        0 -> HomePage(modifier = modifier)
        1 -> SearchPage(modifier = modifier)
        2 -> NotificationsPage(modifier = modifier)
        3 -> SettingsPage(modifier = modifier)
    }
}