package com.example.navitest.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.navitest.api.CartRepository
import com.example.navitest.api.RetrofitClient
import com.example.navitest.model.CartItem
import com.example.navitest.model.Product
import com.example.navitest.utils.PreferencesManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AddItemResult(
    val ok: Boolean,
    val reason: String? = null,
    val added: Int = 0
)

class CartViewModel(
    application: Application,
    private val authViewModel: AuthViewModel
) : AndroidViewModel(application) {
    private val cartRepository = CartRepository(RetrofitClient.apiService)
    private val preferencesManager = PreferencesManager(application)

    private val _items = MutableStateFlow<List<CartItem>>(emptyList())
    val items: StateFlow<List<CartItem>> = _items.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    val total: StateFlow<Double> = MutableStateFlow(0.0).apply {
        viewModelScope.launch {
            _items.collect { items ->
                
                value = items.sumOf { it.finalPrice * it.quantity }
            }
        }
    }
    
    
    val dealSavings: StateFlow<Double> = MutableStateFlow(0.0).apply {
        viewModelScope.launch {
            _items.collect { items ->
                value = items.sumOf { it.totalSavings }
            }
        }
    }

    val descuentoEstudiante: StateFlow<Double> = MutableStateFlow(0.0).apply {
        viewModelScope.launch {
            total.collect { totalValue ->
                val user = authViewModel.currentUser.value
                val isStudent = user?.email?.lowercase()?.endsWith("@duocuc.cl") == true
                value = if (isStudent) totalValue * 0.20 else 0.0
            }
        }
    }

    val totalConDescuento: StateFlow<Double> = MutableStateFlow(0.0).apply {
        viewModelScope.launch {
            total.collect { totalValue ->
                descuentoEstudiante.collect { descuento ->
                    value = totalValue - descuento
                }
            }
        }
    }

    val itemCount: StateFlow<Int> = MutableStateFlow(0).apply {
        viewModelScope.launch {
            _items.collect { items ->
                value = items.sumOf { it.quantity }
            }
        }
    }

    init {
        
        viewModelScope.launch {
            authViewModel.shouldReloadCart.collect {
                loadCart()
            }
        }
    }

    fun loadCart() {
        viewModelScope.launch {
            val token = preferencesManager.getAuthToken()
            if (token.isNullOrBlank()) {
                _items.value = emptyList()
                return@launch
            }

            _loading.value = true
            _error.value = null

            val result = cartRepository.getCart(token)
            result.onSuccess { cartItems ->
                _items.value = cartItems
            }.onFailure { e ->
                _error.value = e.message
                _items.value = emptyList()
            }
            _loading.value = false
        }
    }

    fun addItem(product: Product, quantity: Int = 1): AddItemResult {
        viewModelScope.launch {
            val token = preferencesManager.getAuthToken()
            if (token.isNullOrBlank()) {
                
                return@launch
            }

            
            if (product.stock <= 0) {
                _error.value = "Producto sin stock"
                return@launch
            }

            val existing = _items.value.find { it.id == product.id }
            val currentQty = existing?.quantity ?: 0
            if (currentQty >= product.stock) {
                _error.value = "Límite de stock alcanzado"
                return@launch
            }

            _loading.value = true
            _error.value = null

            val result = cartRepository.addItem(token, product.id, quantity)
            result.onSuccess { cartItems ->
                _items.value = cartItems
            }.onFailure { e ->
                _error.value = e.message
            }
            _loading.value = false
        }
        return AddItemResult(ok = true, added = quantity)
    }

    fun updateQuantity(itemId: String, quantity: Int) {
        viewModelScope.launch {
            val token = preferencesManager.getAuthToken()
            if (token.isNullOrBlank()) return@launch

            _loading.value = true
            _error.value = null

            val result = cartRepository.updateQuantity(token, itemId, quantity)
            result.onSuccess { cartItems ->
                _items.value = cartItems
            }.onFailure { e ->
                _error.value = e.message
            }
            _loading.value = false
        }
    }

    fun removeItem(itemId: String) {
        viewModelScope.launch {
            val token = preferencesManager.getAuthToken()
            if (token.isNullOrBlank()) return@launch

            _loading.value = true
            _error.value = null

            val result = cartRepository.removeItem(token, itemId)
            result.onSuccess { cartItems ->
                _items.value = cartItems
            }.onFailure { e ->
                _error.value = e.message
                
                _items.value = _items.value.filter { it.itemId != itemId }
            }
            _loading.value = false
        }
    }

    fun clear() {
        viewModelScope.launch {
            val token = preferencesManager.getAuthToken()
            if (token.isNullOrBlank()) return@launch

            _loading.value = true
            _error.value = null

            val result = cartRepository.clear(token)
            result.onSuccess {
                _items.value = emptyList()
            }.onFailure { e ->
                _error.value = e.message
                _items.value = emptyList()
            }
            _loading.value = false
        }
    }

    fun clearError() {
        _error.value = null
    }
}

