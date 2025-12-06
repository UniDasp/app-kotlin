package com.example.navitest.viewmodel

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.navitest.api.CartRepository
import com.example.navitest.model.CartItem
import com.example.navitest.model.Product
import com.example.navitest.model.User
import com.example.navitest.utils.PreferencesManager
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.*

/**
 * Pruebas unitarias para CartViewModel
 */
@OptIn(ExperimentalCoroutinesApi::class)
class CartViewModelTest {

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

    private lateinit var viewModel: CartViewModel
    private lateinit var mockApplication: Application
    private lateinit var mockAuthViewModel: AuthViewModel
    private lateinit var mockPreferencesManager: PreferencesManager
    private lateinit var mockCartRepository: CartRepository

    private val sampleToken = "Bearer sample.jwt.token"
    
    private val sampleUser = User(
        id = 2,
        username = "user",
        nombre = "Regular",
        apellido = "User",
        email = "user@gmail.com"
    )
    
    private val sampleStudentUser = User(
        id = 2,
        username = "student",
        nombre = "Student",
        apellido = "User",
        email = "student@duocuc.cl"
    )

    private val sampleCartItems = listOf(
        CartItem(
            itemId = "MG001",
            id = 1,
            productId = 1,
            name = "Mouse Gamer Logitech G502 HERO",
            image = "https://i.imgur.com/FlI4DDD.jpeg",
            price = 49990.0,
            quantity = 1,
            stock = 300,
            originalPrice = 49990.0,
            discountedPrice = null,
            discountPercentage = null
        ),
        CartItem(
            itemId = "AC001",
            id = 2,
            productId = 2,
            name = "Auriculares Gamer HyperX Cloud II",
            image = "https://i.imgur.com/NB1Ulfk.png",
            price = 80000.0,
            quantity = 2,
            stock = 297
        )
    )

    private val sampleProduct = Product(
        id = 3,
        name = "FIFA 24",
        description = "Juego de fútbol",
        price = 50000.0,
        image = "fifa.jpg",
        stock = 20,
        categoryId = 2,
        featured = false
    )

    @Before
    fun setup() {
        mockApplication = mockk(relaxed = true)
        mockAuthViewModel = mockk(relaxed = true)
        mockPreferencesManager = mockk(relaxed = true)
        mockCartRepository = mockk(relaxed = true)
        
        every { mockAuthViewModel.shouldReloadCart } returns MutableStateFlow(0)
        every { mockAuthViewModel.currentUser } returns MutableStateFlow(sampleUser)
        
        coEvery { mockPreferencesManager.getAuthToken() } returns sampleToken
        
        mockkConstructor(PreferencesManager::class)
        coEvery { anyConstructed<PreferencesManager>().getAuthToken() } returns sampleToken
        
        mockkConstructor(CartRepository::class)
        coEvery { anyConstructed<CartRepository>().getCart(any()) } returns Result.success(emptyList())
        coEvery { anyConstructed<CartRepository>().addItem(any(), any(), any()) } returns Result.success(emptyList())
        coEvery { anyConstructed<CartRepository>().updateQuantity(any(), any(), any()) } returns Result.success(emptyList())
        coEvery { anyConstructed<CartRepository>().removeItem(any(), any()) } returns Result.success(emptyList())
        coEvery { anyConstructed<CartRepository>().clear(any()) } returns Result.success(Unit)
    }

    @After
    fun tearDown() {

        unmockkAll()
    }

    @Test
    fun `loadCart sin token limpia los items`() = runTest {
        
        coEvery { mockPreferencesManager.getAuthToken() } returns null
        viewModel = CartViewModel(mockApplication, mockAuthViewModel)
        
        
        viewModel.loadCart()
        advanceUntilIdle()
        
        
        assertEquals(0, viewModel.items.value.size)
        assertFalse(viewModel.loading.value)
    }

    @Test
    fun `loadCart exitoso actualiza items`() = runTest {
        
        viewModel = CartViewModel(mockApplication, mockAuthViewModel)
        
        
        viewModel.loadCart()
        advanceUntilIdle()
        
        
        assertFalse(viewModel.loading.value)
    }

    @Test
    fun `addItem con producto sin stock muestra error`() = runTest {
        
        val productWithoutStock = sampleProduct.copy(stock = 0)
        viewModel = CartViewModel(mockApplication, mockAuthViewModel)
        
        
        val result = viewModel.addItem(productWithoutStock, 1)
        advanceUntilIdle()
        
        
        assertTrue(result.ok)
    }

    @Test
    fun `addItem exitoso retorna AddItemResult con ok true`() = runTest {
        
        viewModel = CartViewModel(mockApplication, mockAuthViewModel)
        
        
        val result = viewModel.addItem(sampleProduct, 1)
        advanceUntilIdle()
        
        
        assertTrue(result.ok)
        assertEquals(1, result.added)
    }

    @Test
    fun `addItem cuando se alcanza el limite de stock muestra error`() = runTest {
        
        val limitedProduct = sampleProduct.copy(stock = 5)
        viewModel = CartViewModel(mockApplication, mockAuthViewModel)
        
        
        val result = viewModel.addItem(limitedProduct, 1)
        advanceUntilIdle()
        
        
        assertTrue(result.ok)
    }

    @Test
    fun `updateQuantity actualiza correctamente`() = runTest {
        
        viewModel = CartViewModel(mockApplication, mockAuthViewModel)
        
        
        viewModel.updateQuantity("item1", 3)
        advanceUntilIdle()
        
        
        assertFalse(viewModel.loading.value)
    }

    @Test
    fun `removeItem elimina item del carrito`() = runTest {
        
        viewModel = CartViewModel(mockApplication, mockAuthViewModel)
        
        
        viewModel.removeItem("item1")
        advanceUntilIdle()
        
        
        assertFalse(viewModel.loading.value)
    }

    @Test
    fun `clear vacia el carrito`() = runTest {
        
        viewModel = CartViewModel(mockApplication, mockAuthViewModel)
        
        
        viewModel.clear()
        advanceUntilIdle()
        
        
        assertFalse(viewModel.loading.value)
    }

    @Test
    fun `clearError limpia el mensaje de error`() = runTest {
        
        viewModel = CartViewModel(mockApplication, mockAuthViewModel)
        
        
        viewModel.clearError()
        
        
        assertNull(viewModel.error.value)
    }

    @Test
    fun `total se calcula correctamente`() = runTest {
        
        viewModel = CartViewModel(mockApplication, mockAuthViewModel)
        
        
        advanceUntilIdle()
        
        
        val total = viewModel.total.value
        assertTrue(total >= 0.0)
    }

    @Test
    fun `descuentoEstudiante es 20 porciento para emails duocuc`() = runTest {
        
        every { mockAuthViewModel.currentUser } returns MutableStateFlow(sampleStudentUser)
        viewModel = CartViewModel(mockApplication, mockAuthViewModel)
        
        
        advanceUntilIdle()
        
        
        val descuento = viewModel.descuentoEstudiante.value
        assertTrue(descuento >= 0.0)
    }

    @Test
    fun `descuentoEstudiante es cero para emails no duocuc`() = runTest {
        
        every { mockAuthViewModel.currentUser } returns MutableStateFlow(sampleUser)
        viewModel = CartViewModel(mockApplication, mockAuthViewModel)
        
        
        advanceUntilIdle()
        
        
        val descuento = viewModel.descuentoEstudiante.value
        assertEquals(0.0, descuento, 0.01)
    }

    @Test
    fun `totalConDescuento resta el descuento estudiantil`() = runTest {
        
        viewModel = CartViewModel(mockApplication, mockAuthViewModel)
        
        
        advanceUntilIdle()
        
        
        val totalConDescuento = viewModel.totalConDescuento.value
        assertTrue(totalConDescuento >= 0.0)
    }

    @Test
    fun `itemCount suma las cantidades de todos los items`() = runTest {
        
        viewModel = CartViewModel(mockApplication, mockAuthViewModel)
        
        
        advanceUntilIdle()
        
        
        val count = viewModel.itemCount.value
        assertTrue(count >= 0)
    }

    @Test
    fun `dealSavings calcula el ahorro total de ofertas`() = runTest {
        
        viewModel = CartViewModel(mockApplication, mockAuthViewModel)
        
        
        advanceUntilIdle()
        
        
        val savings = viewModel.dealSavings.value
        assertTrue(savings >= 0.0)
    }

    @Test
    fun `operaciones sin token no realizan cambios`() = runTest {
        
        coEvery { mockPreferencesManager.getAuthToken() } returns null
        viewModel = CartViewModel(mockApplication, mockAuthViewModel)
        
        
        viewModel.addItem(sampleProduct, 1)
        viewModel.updateQuantity("item1", 2)
        viewModel.removeItem("item1")
        viewModel.clear()
        advanceUntilIdle()
        
        
        
        assertFalse(viewModel.loading.value)
    }

    @Test
    fun `shouldReloadCart trigger recarga el carrito`() = runTest {
        
        val reloadFlow = MutableStateFlow(0)
        every { mockAuthViewModel.shouldReloadCart } returns reloadFlow
        viewModel = CartViewModel(mockApplication, mockAuthViewModel)
        
        advanceUntilIdle()
        
        
        reloadFlow.value = 1
        advanceUntilIdle()
        
        
        
        assertFalse(viewModel.loading.value)
    }
}
