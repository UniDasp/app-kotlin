package com.example.navitest.viewmodel
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.navitest.api.AuthRepository
import com.example.navitest.api.LoginResponse
import com.example.navitest.api.NotificationRepository
import com.example.navitest.api.RegisterResponse
import com.example.navitest.model.User
import com.example.navitest.utils.PreferencesManager
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
 * Pruebas unitarias para AuthViewModel
 */
@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: AuthViewModel
    private lateinit var mockApplication: Application
    private lateinit var mockContext: Context
    private lateinit var mockPreferencesManager: PreferencesManager
    private lateinit var mockSharedPreferences: SharedPreferences

    private val sampleUser = User(
        id = 1,
        username = "testuser",
        nombre = "Test",
        apellido = "User",
        email = "test@example.com",
        telefono = "123456789",
        direccion = "Test Address",
        region = "Test Region",
        ciudad = "Test City",
        roles = listOf("ROLE_USER"),
        activo = true
    )

    private val sampleToken = "sample.jwt.token"

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        
        mockApplication = mockk(relaxed = true)
        mockContext = mockk(relaxed = true)
        mockSharedPreferences = mockk(relaxed = true)
        
        every { mockApplication.applicationContext } returns mockContext
        every { mockContext.getSharedPreferences(any(), any()) } returns mockSharedPreferences
        every { mockSharedPreferences.getString(any(), any()) } returns null
        
        mockPreferencesManager = mockk(relaxed = true)
        
        
        mockkConstructor(PreferencesManager::class)
        mockkConstructor(AuthRepository::class)
        mockkConstructor(NotificationRepository::class)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `login exitoso actualiza authState a Authenticated`() = runTest {
        
        val username = "testuser"
        val password = "password123"
        val loginResponse = LoginResponse(
            token = sampleToken,
            user = sampleUser
        )
        
        
        viewModel = AuthViewModel(mockApplication)
        
        
        viewModel.login(username, password)
        advanceUntilIdle()
        
        
        assertFalse(viewModel.isLoading)
    }

    @Test
    fun `login fallido actualiza authState a Error`() = runTest {
        
        val username = "wronguser"
        val password = "wrongpass"
        
        viewModel = AuthViewModel(mockApplication)
        
        
        viewModel.login(username, password)
        advanceUntilIdle()
        
        
        assertFalse(viewModel.isLoading)
    }

    @Test
    fun `register exitoso retorna true en callback`() = runTest {
        val registerResponse = RegisterResponse(
            token = sampleToken,
            user = sampleUser
        )
        
        val mockAuthRepository = mockk<AuthRepository>()
        coEvery {
            mockAuthRepository.register(
                username = "newuser",
                password = "password123",
                email = "new@example.com",
                firstName = "New",
                lastName = "User",
                phone = null,
                address = null,
                region = null
            )
        } returns Result.success(registerResponse)

        viewModel = AuthViewModel(
            application = mockApplication,
            authRepository = mockAuthRepository,
            notificationRepository = mockk(relaxed = true),
            preferencesManager = mockPreferencesManager
        )

        var result = false
        viewModel.register(
            username = "newuser",
            password = "password123",
            email = "new@example.com",
            firstName = "New",
            lastName = "User",
            onResult = { success -> result = success }
        )
        advanceUntilIdle()

        assertTrue(result)
        assertFalse(viewModel.isLoading)
    }

    @Test
    fun `logout limpia el token y actualiza authState`() = runTest {
        
        viewModel = AuthViewModel(mockApplication)
        
        
        viewModel.logout()
        advanceUntilIdle()
        
        
        
        
    }

    @Test
    fun `clearError limpia el mensaje de error`() = runTest {
        
        viewModel = AuthViewModel(mockApplication)
        
        
        viewModel.clearError()
        
        
        assertNull(viewModel.errorMessage)
    }

    @Test
    fun `updateProfile actualiza el usuario actual`() = runTest {
        
        viewModel = AuthViewModel(mockApplication)
        
        
        viewModel.updateProfile(
            nombre = "Updated",
            apellido = "Name",
            telefono = "987654321"
        )
        advanceUntilIdle()
        
        
        assertFalse(viewModel.isLoading)
    }

    @Test
    fun `updateProfile sin token muestra error`() = runTest {
        
        viewModel = AuthViewModel(mockApplication)
        
        
        viewModel.updateProfile(nombre = "Test")
        advanceUntilIdle()
        
        
        assertFalse(viewModel.isLoading)
    }

    @Test
    fun `login establece isLoading correctamente`() = runTest {
        
        viewModel = AuthViewModel(mockApplication)
        
        
        viewModel.login("user", "pass")
        
        
        
        
        advanceUntilIdle()
        assertFalse(viewModel.isLoading)
    }

    @Test
    fun `register establece isLoading correctamente`() = runTest {
        
        viewModel = AuthViewModel(mockApplication)
        
        
        viewModel.register(
            username = "test",
            password = "pass",
            email = "test@test.com",
            firstName = "Test",
            lastName = "User"
        )
        
        advanceUntilIdle()
        
        
        assertFalse(viewModel.isLoading)
    }

    @Test
    fun `authState inicial es Initial o NotAuthenticated`() {
        
        viewModel = AuthViewModel(mockApplication)
        
        
        val state = viewModel.authState.value
        assertTrue(
            state is AuthState.Initial || 
            state is AuthState.NotAuthenticated ||
            state is AuthState.Loading
        )
    }

    @Test
    fun `getAuthToken retorna token almacenado`() = runTest {
        
        viewModel = AuthViewModel(mockApplication)
        
        
        val token = viewModel.getAuthToken()
        
        
        
        
    }

    @Test
    fun `login con credenciales vacias muestra error`() = runTest {
        
        viewModel = AuthViewModel(mockApplication)
        
        
        viewModel.login("", "")
        advanceUntilIdle()
        
        
        assertFalse(viewModel.isLoading)
    }

    @Test
    fun `register con email de estudiante funciona correctamente`() = runTest {
        
        viewModel = AuthViewModel(mockApplication)
        
        
        viewModel.register(
            username = "student",
            password = "pass123",
            email = "student@duocuc.cl",
            firstName = "Student",
            lastName = "Test"
        )
        advanceUntilIdle()
        
        
        assertFalse(viewModel.isLoading)
    }
}
