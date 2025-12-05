package com.example.navitest

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.navitest.ui.theme.NavitestTheme
import com.example.navitest.utils.BiometricAuthManager
import com.example.navitest.utils.PreferencesManager
import com.example.navitest.viewmodel.AuthState
import com.example.navitest.viewmodel.AuthViewModel
import com.example.navitest.viewmodel.CartViewModel
import com.example.navitest.viewmodel.CartViewModelFactory
import com.example.navitest.viewmodel.ProductsViewModel
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : FragmentActivity() {
    private lateinit var biometricAuthManager: BiometricAuthManager
    private lateinit var preferencesManager: PreferencesManager
    private lateinit var authViewModel: AuthViewModel
    private lateinit var productsViewModel: ProductsViewModel
    private lateinit var cartViewModel: CartViewModel
    
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Log.d("MainActivity", "Notification permission granted")
            setupFirebaseMessaging()
        } else {
            Log.d("MainActivity", "Notification permission denied")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        biometricAuthManager = BiometricAuthManager(this)
        preferencesManager = PreferencesManager(this)
        
        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        productsViewModel = ViewModelProvider(this)[ProductsViewModel::class.java]
        
        val cartFactory = CartViewModelFactory(application, authViewModel)
        cartViewModel = ViewModelProvider(this, cartFactory)[CartViewModel::class.java]

        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        } else {
            setupFirebaseMessaging()
        }
        
        
        handleDeepLink(intent)

        lifecycleScope.launch {
            val isBiometricEnabled = preferencesManager.isBiometricEnabled.first()
            val isLoggedIn = preferencesManager.isLoggedIn.first()

            if (isBiometricEnabled && isLoggedIn) {
                checkBiometricAuth()
            } else {
                showMainContent()
            }
        }
    }
    
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleDeepLink(intent)
    }
    
    private fun handleDeepLink(intent: Intent?) {
        val data: Uri? = intent?.data
        
        
        val notificationType = intent?.getStringExtra("notification_type")
        if (notificationType == "deal") {
            val dealType = intent.getStringExtra("deal_type") ?: "ALL"
            val dealId = intent.getStringExtra("deal_id")
            
            Log.d("MainActivity", "Deal notification opened - type: $dealType, id: $dealId")
            
            
            handleDealNavigation(dealType, dealId)
            return
        }
        
        data?.let {
            Log.d("MainActivity", "Deep link received: $it")
            
            if (it.path?.contains("reset-password") == true) {
                val token = it.getQueryParameter("token")
                Log.d("MainActivity", "Reset password token: $token")
                
                
                if (token != null) {
                    showResetPasswordScreen(token)
                }
            }
        }
    }
    
    private fun handleDealNavigation(dealType: String, dealId: String?) {
        when (dealType) {
            "ALL" -> {
                
                Log.d("MainActivity", "Navigating to all products")
                
                showMainContentWithDeals()
            }
            "CATEGORY" -> {
                
                Log.d("MainActivity", "Navigating to category")
                
            }
            "SPECIFIC_PRODUCT" -> {
                
                Log.d("MainActivity", "Navigating to product: $dealId")
                
            }
        }
    }
    
    private fun showMainContentWithDeals() {
        
        showMainContent()
    }
    
    private fun showResetPasswordScreen(token: String) {
        setContent {
            NavitestTheme {
                ResetPasswordScreen(token = token) {
                    
                    showMainContent()
                }
            }
        }
    }
    
    private fun setupFirebaseMessaging() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("MainActivity", "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }
            
            val token = task.result
            Log.d("MainActivity", "FCM Token: $token")
            
            
            val sharedPreferences = getSharedPreferences("levelup_prefs", MODE_PRIVATE)
            sharedPreferences.edit().putString("fcm_token", token).apply()
        }
    }


    private fun checkBiometricAuth() {
        if (biometricAuthManager.isBiometricAvailable()) {
            var isAuthenticated = false

            setContent {
                NavitestTheme {
                    BiometricPromptScreen(
                        onAuthenticate = {
                            if (!isAuthenticated) {
                                isAuthenticated = true
                                biometricAuthManager.authenticate(
                                    activity = this,
                                    title = "Autenticación requerida",
                                    subtitle = "Verifica tu identidad",
                                    description = "Usa tu huella digital o patrón para acceder",
                                    onSuccess = {
                                        showMainContent()
                                    },
                                    onError = { error ->
                                        Toast.makeText(this, "Error: $error", Toast.LENGTH_SHORT).show()
                                    },
                                    onFailed = {
                                        Toast.makeText(this, "Autenticación fallida", Toast.LENGTH_SHORT).show()
                                    }
                                )
                            }
                        }
                    )
                }
            }
        } else {
            showMainContent()
        }
    }

    private fun showMainContent() {
        setContent {
            NavitestTheme {
                AppContent(
                    authViewModel = authViewModel,
                    productsViewModel = productsViewModel,
                    cartViewModel = cartViewModel,
                    preferencesManager = preferencesManager,
                    biometricAuthManager = biometricAuthManager
                )
            }
        }
    }
}

@Composable
fun BiometricPromptScreen(onAuthenticate: () -> Unit) {
    LaunchedEffect(Unit) {
        onAuthenticate()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Autenticación biométrica",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Por favor, verifica tu identidad",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun AppContent(
    authViewModel: AuthViewModel,
    productsViewModel: ProductsViewModel,
    cartViewModel: CartViewModel,
    preferencesManager: PreferencesManager,
    biometricAuthManager: BiometricAuthManager
) {
    val authState by authViewModel.authState.collectAsState()
    var showRegister by remember { mutableStateOf(false) }

    when (authState) {
        is AuthState.Initial -> {
            LoadingScreen()
        }
        is AuthState.Loading -> {
            LoadingScreen()
        }
        is AuthState.Authenticated -> {
            PantallaInicialWithViewModels(
                authViewModel = authViewModel,
                productsViewModel = productsViewModel,
                cartViewModel = cartViewModel,
                preferencesManager = preferencesManager,
                biometricAuthManager = biometricAuthManager
            )
        }
        is AuthState.NotAuthenticated -> {
            if (showRegister) {
                com.example.navitest.pages.RegisterPage(
                    authViewModel = authViewModel,
                    onNavigateToLogin = { showRegister = false },
                    onRegisterSuccess = { /* Will automatically navigate to authenticated state */ }
                )
            } else {
                com.example.navitest.pages.LoginScreen(
                    authViewModel = authViewModel,
                    onNavigateToRegister = { showRegister = true }
                )
            }
        }
        is AuthState.Error -> {
            if (showRegister) {
                com.example.navitest.pages.RegisterPage(
                    authViewModel = authViewModel,
                    onNavigateToLogin = { showRegister = false },
                    onRegisterSuccess = { /* Will automatically navigate to authenticated state */ }
                )
            } else {
                com.example.navitest.pages.LoginScreen(
                    authViewModel = authViewModel,
                    onNavigateToRegister = { showRegister = true }
                )
            }
        }
    }
}

@Composable
fun LoadingScreen() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            
            Text(
                text = "🎮",
                style = MaterialTheme.typography.displayLarge.copy(
                    fontSize = 80.sp
                )
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "LEVEL-UP GAMER",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp),
                color = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Cargando...",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun ResetPasswordScreen(token: String, onComplete: () -> Unit) {
    val viewModel: com.example.navitest.viewmodel.ResetPasswordViewModel = 
        androidx.lifecycle.viewmodel.compose.viewModel()
    val uiState by viewModel.uiState.collectAsState()
    
    LaunchedEffect(uiState.successMessage) {
        if (uiState.successMessage.isNotEmpty()) {
            kotlinx.coroutines.delay(3000)
            onComplete()
        }
    }
    
    com.example.navitest.pages.ResetPasswordPage(
        newPassword = uiState.newPassword,
        confirmPassword = uiState.confirmPassword,
        onNewPasswordChange = viewModel::updateNewPassword,
        onConfirmPasswordChange = viewModel::updateConfirmPassword,
        isLoading = uiState.isLoading,
        isError = uiState.isError,
        errorMessage = uiState.errorMessage,
        successMessage = uiState.successMessage,
        onResetClick = { viewModel.resetPassword(token) }
    )
}
