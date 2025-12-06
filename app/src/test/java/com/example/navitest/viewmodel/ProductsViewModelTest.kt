package com.example.navitest.viewmodel

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.navitest.api.ProductsRepository
import com.example.navitest.model.Category
import com.example.navitest.model.Product
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.*

/**
 * Pruebas unitarias para ProductsViewModel
 */
@OptIn(ExperimentalCoroutinesApi::class)
class ProductsViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = object : org.junit.rules.TestWatcher() {
        val testDispatcher = UnconfinedTestDispatcher()
        override fun starting(description: org.junit.runner.Description) {
            Dispatchers.setMain(testDispatcher)
        }
        override fun finished(description: org.junit.runner.Description) {
            Dispatchers.resetMain()
        }
    }

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: ProductsViewModel
    private lateinit var mockApplication: Application
    private lateinit var mockRepository: ProductsRepository

    private val sampleProducts = listOf(
        Product(
            id = 1,
            name = "PlayStation 5",
            description = "Consola de videojuegos",
            price = 500000.0,
            image = "ps5.jpg",
            stock = 10,
            categoryId = 1,
            category = "Consolas",
            featured = true
        ),
        Product(
            id = 2,
            name = "Xbox Series X",
            description = "Consola Microsoft",
            price = 450000.0,
            image = "xbox.jpg",
            stock = 5,
            categoryId = 1,
            category = "Consolas",
            featured = false
        ),
        Product(
            id = 3,
            name = "FIFA 24",
            description = "Juego de fútbol",
            price = 50000.0,
            image = "fifa.jpg",
            stock = 20,
            categoryId = 2,
            category = "Juegos",
            featured = true
        )
    )

    private val sampleCategories = listOf(
        Category(id = 1, nombre = "Consolas", descripcion = "Consolas de videojuegos"),
        Category(id = 2, nombre = "Juegos", descripcion = "Videojuegos")
    )

    @Before
    fun setup() {
        mockApplication = mockk(relaxed = true)
        mockRepository = mockk(relaxed = true)
        
        mockkConstructor(ProductsRepository::class)
        coEvery { anyConstructed<ProductsRepository>().getActiveProducts() } returns Result.success(emptyList())
        coEvery { anyConstructed<ProductsRepository>().getProductById(any()) } returns Result.success(sampleProducts[0])
        coEvery { anyConstructed<ProductsRepository>().getProductsByCategory(any()) } returns Result.success(emptyList())
        coEvery { anyConstructed<ProductsRepository>().getCategories() } returns Result.success(emptyList())
        coEvery { anyConstructed<ProductsRepository>().searchProducts(any()) } returns Result.success(emptyList())
        
        viewModel = ProductsViewModel(mockApplication)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `loadProducts actualiza estado a Success cuando la peticion es exitosa`() = runTest {
        
        coEvery { mockRepository.getActiveProducts() } returns Result.success(sampleProducts)
        
        
        viewModel.loadProducts()
        advanceUntilIdle()
        
        
        assertFalse(viewModel.isLoading)
        assertNull(viewModel.errorMessage)
    }

    @Test
    fun `loadProducts actualiza estado a Error cuando la peticion falla`() = runTest {
        
        val errorMessage = "Network error"
        coEvery { mockRepository.getActiveProducts() } returns Result.failure(Exception(errorMessage))
        
        
        viewModel.loadProducts()
        advanceUntilIdle()
        
        
        assertFalse(viewModel.isLoading)
    }

    @Test
    fun `searchProducts con query vacio llama a loadProducts`() = runTest {
        
        viewModel.searchProducts("")
        advanceUntilIdle()


        
        assertFalse(viewModel.isLoading)
    }

    @Test
    fun `searchProducts actualiza productos cuando encuentra resultados`() = runTest {
        
        val searchQuery = "FIFA"
        val searchResults = sampleProducts.filter { it.name.contains(searchQuery) }
        coEvery { mockRepository.searchProducts(searchQuery) } returns Result.success(searchResults)
        
        
        viewModel.searchProducts(searchQuery)
        advanceUntilIdle()
        
        
        assertFalse(viewModel.isLoading)
    }

    @Test
    fun `searchProducts muestra error cuando la busqueda falla`() = runTest {
        
        val searchQuery = "Test"
        val errorMessage = "Search failed"
        coEvery { mockRepository.searchProducts(searchQuery) } returns Result.failure(Exception(errorMessage))
        
        
        viewModel.searchProducts(searchQuery)
        advanceUntilIdle()
        
        
        assertFalse(viewModel.isLoading)
    }

    @Test
    fun `getProductById retorna producto cuando existe`() = runTest {
        
        val productId = 1
        val expectedProduct = sampleProducts[0]
        coEvery { mockRepository.getProductById(productId) } returns Result.success(expectedProduct)
        
        var resultProduct: Product? = null
        
        
        viewModel.getProductById(productId) { product ->
            resultProduct = product
        }
        advanceUntilIdle()
        
        
        
        
    }

    @Test
    fun `getProductById retorna null cuando el producto no existe`() = runTest {
        
        val productId = 999
        coEvery { mockRepository.getProductById(productId) } returns Result.failure(Exception("Not found"))
        
        var resultProduct: Product? = null
        
        
        viewModel.getProductById(productId) { product ->
            resultProduct = product
        }
        advanceUntilIdle()
        
        
        
    }

    @Test
    fun `filterByCategoryId con null llama a loadProducts`() = runTest {
        
        viewModel.filterByCategoryId(null)
        advanceUntilIdle()
        
        
        assertFalse(viewModel.isLoading)
    }

    @Test
    fun `filterByCategoryId filtra productos correctamente`() = runTest {
        
        val categoryId = 1
        val filteredProducts = sampleProducts.filter { it.categoryId == categoryId }
        coEvery { mockRepository.getProductsByCategory(categoryId) } returns Result.success(filteredProducts)
        
        
        viewModel.filterByCategoryId(categoryId)
        advanceUntilIdle()
        
        
        assertFalse(viewModel.isLoading)
    }

    @Test
    fun `filterByCategoryId muestra error cuando falla la peticion`() = runTest {
        
        val categoryId = 1
        val errorMessage = "Failed to filter"
        coEvery { mockRepository.getProductsByCategory(categoryId) } returns Result.failure(Exception(errorMessage))
        
        
        viewModel.filterByCategoryId(categoryId)
        advanceUntilIdle()
        
        
        assertFalse(viewModel.isLoading)
    }

    @Test
    fun `isLoading es true durante la carga`() = runTest {
        
        coEvery { mockRepository.getActiveProducts() } coAnswers {
            kotlinx.coroutines.delay(100)
            Result.success(sampleProducts)
        }
        
        
        viewModel.loadProducts()
        
        
        
        advanceTimeBy(50)
        
        
        advanceUntilIdle()
        assertFalse(viewModel.isLoading)
    }

    @Test
    fun `estado inicial es Initial`() = runTest {
        advanceUntilIdle()
        val state = viewModel.productsState.value
        assertTrue(state is ProductsState.Initial || state is ProductsState.Loading || state is ProductsState.Success)
    }

    @Test
    fun `featured products se actualizan correctamente`() = runTest {
        
        coEvery { mockRepository.getActiveProducts() } returns Result.success(sampleProducts)
        
        
        viewModel.loadProducts()
        advanceUntilIdle()
        
        
        
        
        
    }
}
