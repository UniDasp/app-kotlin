package com.example.navitest.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.navitest.api.ProductsRepository
import com.example.navitest.model.Category
import com.example.navitest.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class ProductsState {
    object Initial : ProductsState()
    object Loading : ProductsState()
    data class Success(val products: List<Product>) : ProductsState()
    data class Error(val message: String) : ProductsState()
}

class ProductsViewModel(application: Application) : AndroidViewModel(application) {
    private val productsRepository = ProductsRepository()

    private val _productsState = MutableStateFlow<ProductsState>(ProductsState.Initial)
    val productsState: StateFlow<ProductsState> = _productsState.asStateFlow()

    private val _allProducts = MutableStateFlow<List<Product>>(emptyList())
    val allProducts: StateFlow<List<Product>> = _allProducts.asStateFlow()

    private val _featuredProducts = MutableStateFlow<List<Product>>(emptyList())
    val featuredProducts: StateFlow<List<Product>> = _featuredProducts.asStateFlow()

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    init {
        loadProducts()
        loadCategories()
    }

    fun loadProducts() {
        viewModelScope.launch {
            isLoading = true
            _productsState.value = ProductsState.Loading

            val result = productsRepository.getActiveProducts()
            
            if (result.isSuccess) {
                val products = result.getOrNull() ?: emptyList()
                _allProducts.value = products
                _featuredProducts.value = products.filter { it.featured }
                _productsState.value = ProductsState.Success(products)
            } else {
                errorMessage = result.exceptionOrNull()?.message ?: "Error al cargar productos"
                _productsState.value = ProductsState.Error(errorMessage!!)
            }
            
            isLoading = false
        }
    }

    fun searchProducts(query: String) {
        if (query.isBlank()) {
            loadProducts()
            return
        }

        viewModelScope.launch {
            isLoading = true
            _productsState.value = ProductsState.Loading

            val result = productsRepository.searchProducts(query)
            
            if (result.isSuccess) {
                val products = result.getOrNull() ?: emptyList()
                _allProducts.value = products
                _productsState.value = ProductsState.Success(products)
            } else {
                errorMessage = result.exceptionOrNull()?.message ?: "Error en la búsqueda"
                _productsState.value = ProductsState.Error(errorMessage!!)
            }
            
            isLoading = false
        }
    }

    fun getProductById(id: Int, onResult: (Product?) -> Unit) {
        viewModelScope.launch {
            val result = productsRepository.getProductById(id)
            onResult(result.getOrNull())
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            val result = productsRepository.getCategories()
            if (result.isSuccess) {
                _categories.value = result.getOrNull() ?: emptyList()
            }
        }
    }

    fun filterByCategoryId(categoryId: Int?) {
        if (categoryId == null) {
            loadProducts()
            return
        }

        viewModelScope.launch {
            isLoading = true
            _productsState.value = ProductsState.Loading

            val result = productsRepository.getProductsByCategory(categoryId)
            
            if (result.isSuccess) {
                val products = result.getOrNull() ?: emptyList()
                _allProducts.value = products
                _productsState.value = ProductsState.Success(products)
            } else {
                errorMessage = result.exceptionOrNull()?.message ?: "Error al filtrar productos"
                _productsState.value = ProductsState.Error(errorMessage!!)
            }
            
            isLoading = false
        }
    }
}
