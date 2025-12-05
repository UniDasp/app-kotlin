package com.example.navitest

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.navitest.pages.*
import com.example.navitest.utils.BiometricAuthManager
import com.example.navitest.utils.PreferencesManager
import com.example.navitest.viewmodel.AuthViewModel
import com.example.navitest.viewmodel.CartViewModel
import com.example.navitest.viewmodel.ProductsViewModel

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun PantallaInicialWithViewModels(
    authViewModel: AuthViewModel,
    productsViewModel: ProductsViewModel,
    cartViewModel: CartViewModel,
    preferencesManager: PreferencesManager,
    biometricAuthManager: BiometricAuthManager
) {
    val navItemList = listOf(
        ItemsNav("Home", Icons.Default.Home),
        ItemsNav("Productos", Icons.Default.Search),
        ItemsNav("Carrito", Icons.Default.ShoppingCart),
        ItemsNav("Cuenta", Icons.Default.Person),
    )

    var selectedIndex by remember { mutableIntStateOf(0) }
    var showCheckout by remember { mutableStateOf(false) }
    var showPayments by remember { mutableStateOf(false) }
    val cartItemCount by cartViewModel.itemCount.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (!showCheckout && !showPayments) {
                CenterAlignedTopAppBar(
                    title = { Text("🎮 LEVEL-UP GAMER") }
                )
            }
        },
        bottomBar = {
            if (!showCheckout && !showPayments) {
                NavigationBar {
                navItemList.forEachIndexed { index, nav ->
                    NavigationBarItem(
                        selected = selectedIndex == index,
                        onClick = { selectedIndex = index },
                        icon = {
                            if (index == 2 && cartItemCount > 0) {
                                BadgedBox(
                                    badge = { Badge { Text("$cartItemCount") } }
                                ) {
                                    Icon(imageVector = nav.icon, contentDescription = nav.label)
                                }
                            } else {
                                Icon(imageVector = nav.icon, contentDescription = nav.label)
                            }
                        },
                        label = { Text(nav.label) }
                    )
                }
            }
            }
        }
    ) { innerPadding ->
        when {
            showCheckout -> {
                CheckoutPage(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    cartViewModel = cartViewModel,
                    authViewModel = authViewModel,
                    onNavigateBack = { 
                        showCheckout = false
                        selectedIndex = 0
                    },
                    onNavigateToProducts = {
                        showCheckout = false
                        selectedIndex = 1
                    }
                )
            }
            showPayments -> {
                PaymentsPage(
                    modifier = Modifier.fillMaxSize(),
                    authViewModel = authViewModel,
                    onNavigateBack = { showPayments = false }
                )
            }
            else -> {
                ContenidoPantallaWithViewModels(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    selectedIndex = selectedIndex,
                    authViewModel = authViewModel,
                    productsViewModel = productsViewModel,
                    cartViewModel = cartViewModel,
                    preferencesManager = preferencesManager,
                    biometricAuthManager = biometricAuthManager,
                    onNavigateToCheckout = { showCheckout = true },
                    onNavigateToPayments = { showPayments = true }
                )
            }
        }
    }
}

@Composable
fun ContenidoPantallaWithViewModels(
    modifier: Modifier = Modifier,
    selectedIndex: Int,
    authViewModel: AuthViewModel,
    productsViewModel: ProductsViewModel,
    cartViewModel: CartViewModel,
    preferencesManager: PreferencesManager,
    biometricAuthManager: BiometricAuthManager,
    onNavigateToCheckout: () -> Unit,
    onNavigateToPayments: () -> Unit
) {
    when (selectedIndex) {
        0 -> HomePage(
            modifier = modifier,
            productsViewModel = productsViewModel,
            cartViewModel = cartViewModel
        )
        1 -> SearchPage(
            modifier = modifier,
            productsViewModel = productsViewModel,
            cartViewModel = cartViewModel
        )
        2 -> CartPage(
            modifier = modifier,
            cartViewModel = cartViewModel,
            authViewModel = authViewModel,
            onNavigateToCheckout = onNavigateToCheckout
        )
        3 -> AccountPage(
            modifier = modifier,
            authViewModel = authViewModel,
            preferencesManager = preferencesManager,
            biometricAuthManager = biometricAuthManager,
            onNavigateToPayments = onNavigateToPayments
        )
    }
}
