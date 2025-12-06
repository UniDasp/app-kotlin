package com.example.navitest.api

import com.example.navitest.model.User
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import retrofit2.Response

 
@OptIn(ExperimentalCoroutinesApi::class)
class
AuthRepositoryTest {

    private lateinit var repository: AuthRepository
    private lateinit var mockApiService: ApiService

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
        mockApiService = mockk()
        
        
        mockkObject(RetrofitClient)
        every { RetrofitClient.apiService } returns mockApiService
        
        repository = AuthRepository()
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `login exitoso retorna LoginResponse con token`() = runTest {
        
        val username = "testuser"
        val password = "password123"
        val loginResponse = LoginResponse(token = sampleToken, user = sampleUser)
        
        coEvery { 
            mockApiService.login(LoginRequest(username, password)) 
        } returns Response.success(loginResponse)
        
        
        val result = repository.login(username, password)
        
        
        assertTrue(result.isSuccess)
        val response = result.getOrNull()
        assertNotNull(response)
        assertEquals(sampleToken, response?.token)
        assertEquals(sampleUser, response?.user)
    }

    @Test
    fun `login fallido retorna error`() = runTest {
        
        val username = "wronguser"
        val password = "wrongpass"
        
        coEvery { 
            mockApiService.login(any()) 
        } returns Response.error(401, "Unauthorized".toResponseBody())
        
        
        val result = repository.login(username, password)
        
        
        assertTrue(result.isFailure)
        assertNotNull(result.exceptionOrNull())
    }

    @Test
    fun `login maneja excepciones de red`() = runTest {
        
        coEvery { mockApiService.login(any()) } throws Exception("Network error")
        
        
        val result = repository.login("user", "pass")
        
        
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()?.message?.contains("Network error") ?: false)
    }

    @Test
    fun `register exitoso retorna RegisterResponse`() = runTest {
        
        val registerRequest = RegisterRequest(
            username = "newuser",
            password = "pass123",
            email = "new@example.com",
            firstName = "New",
            lastName = "User"
        )
        val registerResponse = RegisterResponse(
            token = sampleToken,
            user = sampleUser
        )
        
        coEvery { 
            mockApiService.register(any()) 
        } returns Response.success(registerResponse)
        
        
        val result = repository.register(
            username = "newuser",
            password = "pass123",
            email = "new@example.com",
            firstName = "New",
            lastName = "User"
        )
        
        
        assertTrue(result.isSuccess)
        assertNotNull(result.getOrNull())
    }

    @Test
    fun `register con datos opcionales funciona correctamente`() = runTest {
        
        val registerResponse = RegisterResponse(token = sampleToken, user = sampleUser)
        coEvery { mockApiService.register(any()) } returns Response.success(registerResponse)
        
        
        val result = repository.register(
            username = "user",
            password = "pass",
            email = "email@test.com",
            firstName = "First",
            lastName = "Last",
            phone = "123456789",
            address = "Address",
            region = "Region"
        )
        
        
        assertTrue(result.isSuccess)
    }

    @Test
    fun `register fallido retorna error`() = runTest {
        
        coEvery { 
            mockApiService.register(any()) 
        } returns Response.error(400, "Bad request".toResponseBody())
        
        
        val result = repository.register(
            username = "user",
            password = "pass",
            email = "email@test.com",
            firstName = "First",
            lastName = "Last"
        )
        
        
        assertTrue(result.isFailure)
    }

    @Test
    fun `getCurrentUser retorna usuario actual`() = runTest {
        
        coEvery { 
            mockApiService.getCurrentUser("Bearer $sampleToken") 
        } returns Response.success(sampleUser)
        
        
        val result = repository.getCurrentUser(sampleToken)
        
        
        assertTrue(result.isSuccess)
        val user = result.getOrNull()
        assertNotNull(user)
        assertEquals(1, user?.id)
        assertEquals("testuser", user?.username)
    }

    @Test
    fun `getCurrentUser agrega Bearer si no esta presente`() = runTest {
        
        coEvery { 
            mockApiService.getCurrentUser("Bearer $sampleToken") 
        } returns Response.success(sampleUser)
        
        
        val result = repository.getCurrentUser(sampleToken)
        
        
        assertTrue(result.isSuccess)
        coVerify { mockApiService.getCurrentUser("Bearer $sampleToken") }
    }

    @Test
    fun `getCurrentUser no duplica Bearer si ya esta presente`() = runTest {
        
        val tokenWithBearer = "Bearer $sampleToken"
        coEvery { 
            mockApiService.getCurrentUser(tokenWithBearer) 
        } returns Response.success(sampleUser)
        
        
        val result = repository.getCurrentUser(tokenWithBearer)
        
        
        assertTrue(result.isSuccess)
        coVerify(exactly = 1) { mockApiService.getCurrentUser(tokenWithBearer) }
    }

    @Test
    fun `changePassword exitoso retorna mensaje`() = runTest {
        
        val messageResponse = MessageResponse("Password changed successfully")
        coEvery { 
            mockApiService.changePassword(any(), any()) 
        } returns Response.success(messageResponse)
        
        
        val result = repository.changePassword(sampleToken, "oldpass", "newpass")
        
        
        assertTrue(result.isSuccess)
        assertEquals("Password changed successfully", result.getOrNull()?.message)
    }

    @Test
    fun `changePassword fallido retorna error`() = runTest {
        
        coEvery { 
            mockApiService.changePassword(any(), any()) 
        } returns Response.error(400, "Wrong password".toResponseBody())
        
        
        val result = repository.changePassword(sampleToken, "wrong", "new")
        
        
        assertTrue(result.isFailure)
    }

    @Test
    fun `validateToken retorna ValidateResponse`() = runTest {
        
        val validateResponse = ValidateResponse(valid = true)
        coEvery { 
            mockApiService.validate("Bearer $sampleToken") 
        } returns Response.success(validateResponse)
        
        
        val result = repository.validateToken(sampleToken)
        
        
        assertTrue(result.isSuccess)
        assertEquals(true, result.getOrNull()?.valid)
    }

    @Test
    fun `updateProfile actualiza usuario correctamente`() = runTest {
        
        val updatedUser = sampleUser.copy(nombre = "Updated", apellido = "Name")
        coEvery { 
            mockApiService.updateUser(any(), any(), any()) 
        } returns Response.success(updatedUser)
        
        
        val result = repository.updateProfile(sampleToken, 1, updatedUser)
        
        
        assertTrue(result.isSuccess)
        assertEquals("Updated", result.getOrNull()?.nombre)
        assertEquals("Name", result.getOrNull()?.apellido)
    }

    @Test
    fun `verifyEmail exitoso retorna mensaje`() = runTest {
        
        val messageResponse = MessageResponse("Email verified")
        coEvery { 
            mockApiService.verifyEmail(any(), any()) 
        } returns Response.success(messageResponse)
        
        
        val result = repository.verifyEmail(sampleToken, "123456")
        
        
        assertTrue(result.isSuccess)
        assertEquals("Email verified", result.getOrNull()?.message)
    }

    @Test
    fun `resendVerificationCode envia codigo correctamente`() = runTest {
        
        val messageResponse = MessageResponse("Code sent")
        coEvery { 
            mockApiService.resendVerificationCode(any()) 
        } returns Response.success(messageResponse)
        
        
        val result = repository.resendVerificationCode(sampleToken)
        
        
        assertTrue(result.isSuccess)
    }

    @Test
    fun `forgotPassword solicita recuperacion exitosamente`() = runTest {
        
        val messageResponse = MessageResponse("Recovery email sent")
        coEvery { 
            mockApiService.forgotPassword(any()) 
        } returns Response.success(messageResponse)
        
        
        val result = repository.forgotPassword("test@example.com")
        
        
        assertTrue(result.isSuccess)
        assertEquals("Recovery email sent", result.getOrNull()?.message)
    }

    @Test
    fun `resetPasswordWithToken resetea contrasena exitosamente`() = runTest {
        
        val messageResponse = MessageResponse("Password reset successfully")
        coEvery { 
            mockApiService.resetPasswordWithToken(any()) 
        } returns Response.success(messageResponse)
        
        
        val result = repository.resetPasswordWithToken("resetToken", "newPassword123")
        
        
        assertTrue(result.isSuccess)
    }

    @Test
    fun `requestVerificationByUsername solicita verificacion`() = runTest {
        
        val messageResponse = MessageResponse("Verification requested")
        coEvery { 
            mockApiService.requestVerificationByUsername(any()) 
        } returns Response.success(messageResponse)
        
        
        val result = repository.requestVerificationByUsername("testuser")
        
        
        assertTrue(result.isSuccess)
    }

    @Test
    fun `manejo de errores de red en todas las operaciones`() = runTest {
        
        coEvery { mockApiService.getCurrentUser(any()) } throws Exception("Connection timeout")
        coEvery { mockApiService.changePassword(any(), any()) } throws Exception("Network error")
        coEvery { mockApiService.updateUser(any(), any(), any()) } throws Exception("Server error")
        
        
        val result1 = repository.getCurrentUser(sampleToken)
        val result2 = repository.changePassword(sampleToken, "old", "new")
        val result3 = repository.updateProfile(sampleToken, 1, sampleUser)
        
        
        assertTrue(result1.isFailure)
        assertTrue(result2.isFailure)
        assertTrue(result3.isFailure)
    }
}
