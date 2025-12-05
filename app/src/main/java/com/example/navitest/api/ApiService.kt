package com.example.navitest.api

import com.example.navitest.model.Deal
import com.example.navitest.model.Product
import com.example.navitest.model.User
import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    
    @POST("/autenticacion/login")
    suspend fun login(@Body credentials: LoginRequest): Response<LoginResponse>

    @POST("/autenticacion/registrar")
    suspend fun register(@Body body: RegisterRequest): Response<RegisterResponse>

    @POST("/autenticacion/refrescar")
    suspend fun refresh(@Header("Authorization") token: String): Response<RefreshResponse>

    @GET("/autenticacion/validar")
    suspend fun validate(@Header("Authorization") token: String): Response<ValidateResponse>

    @GET("/autenticacion/yo")
    suspend fun getCurrentUser(@Header("Authorization") token: String): Response<User>

    @PUT("/usuarios/{id}")
    suspend fun updateUser(
        @Header("Authorization") token: String,
        @Path("id") userId: Int,
        @Body body: User
    ): Response<User>

    @POST("/autenticacion/cambiar-contrasena")
    suspend fun changePassword(
        @Header("Authorization") token: String,
        @Body body: ChangePasswordRequest
    ): Response<MessageResponse>

    @POST("/autenticacion/recuperar")
    suspend fun recoverPassword(@Body body: RecoverPasswordRequest): Response<MessageResponse>

    @POST("/autenticacion/reset")
    suspend fun resetPassword(@Body body: ResetPasswordRequest): Response<MessageResponse>
    
    
    @POST("/api/auth/verificar-email")
    suspend fun verifyEmail(
        @Header("Authorization") token: String,
        @Body body: VerifyEmailRequest
    ): Response<MessageResponse>
    
    @POST("/api/auth/reenviar-verificacion")
    suspend fun resendVerificationCode(
        @Header("Authorization") token: String
    ): Response<MessageResponse>
    
    @POST("/api/auth/solicitar-verificacion")
    suspend fun requestVerificationByUsername(
        @Body body: RequestVerificationRequest
    ): Response<MessageResponse>
    
    
    @POST("/api/auth/forgot-password")
    suspend fun forgotPassword(
        @Body body: ForgotPasswordRequest
    ): Response<MessageResponse>
    
    @POST("/api/auth/reset-password")
    suspend fun resetPasswordWithToken(
        @Body body: ResetPasswordWithTokenRequest
    ): Response<MessageResponse>
    
    
    @POST("/api/notifications/register-device")
    suspend fun registerDevice(
        @Header("Authorization") token: String,
        @Body body: RegisterDeviceRequest
    ): Response<MessageResponse>
    
    @DELETE("/api/notifications/remove-device/{platform}")
    suspend fun removeDevice(
        @Header("Authorization") token: String,
        @Path("platform") platform: String
    ): Response<MessageResponse>

    
    @GET("/api/deals/active")
    suspend fun getActiveDeals(): Response<List<Deal>>

    @GET("/api/deals/{id}")
    suspend fun getDealById(@Path("id") id: Int): Response<Deal>

    
    @GET("/productos")
    suspend fun listProducts(): Response<List<ProductDto>>

    @GET("/productos/activos")
    suspend fun listActiveProducts(): Response<List<ProductDto>>

    @GET("/productos/categoria/{categoriaId}")
    suspend fun getProductsByCategory(@Path("categoriaId") categoriaId: Int): Response<List<ProductDto>>

    @GET("/productos/buscar")
    suspend fun searchProducts(@Query("consulta") query: String): Response<List<ProductDto>>

    @GET("/productos/{id}")
    suspend fun getProductById(@Path("id") id: Int): Response<ProductDto>

    
    @GET("/categorias")
    suspend fun listCategories(): Response<List<CategoryDto>>

    @GET("/categorias/{id}")
    suspend fun getCategoryById(@Path("id") id: Int): Response<CategoryDto>

    
    @GET("/api/cart")
    suspend fun getCart(@Header("Authorization") token: String): Response<CartResponse>

    @POST("/api/cart/items")
    suspend fun addItemToCart(
        @Header("Authorization") token: String,
        @Body body: AddToCartRequest
    ): Response<CartResponse>

    @PUT("/api/cart/items/{itemId}")
    suspend fun updateCartItem(
        @Header("Authorization") token: String,
        @Path("itemId") itemId: String,
        @Body body: UpdateCartItemRequest
    ): Response<CartResponse>

    @DELETE("/api/cart/items/{itemId}")
    suspend fun removeCartItem(
        @Header("Authorization") token: String,
        @Path("itemId") itemId: String
    ): Response<CartResponse>

    @DELETE("/api/cart")
    suspend fun clearCart(@Header("Authorization") token: String): Response<MessageResponse>

    
    @POST("/pagos")
    suspend fun initiatePayment(
        @Header("Authorization") token: String,
        @Body body: InitiatePaymentRequest
    ): Response<PaymentResponse>

    @POST("/pagos/{id}/confirmar")
    suspend fun confirmPayment(
        @Path("id") id: String,
        @Header("Authorization") paymentToken: String?,
        @Body body: ConfirmPaymentRequest
    ): Response<PaymentResponse>
    
    @GET("/pagos/mis-pagos")
    suspend fun getMyPayments(@Header("Authorization") token: String): Response<List<PaymentResponse>>
}


data class LoginRequest(val username: String, val password: String)
data class LoginResponse(val token: String, val user: User? = null)

data class RegisterRequest(
    val username: String,
    val password: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val phone: String? = null,
    val address: String? = null,
    val region: String? = null
)
data class RegisterResponse(val token: String, val user: User)

data class RefreshResponse(val token: String)
data class ValidateResponse(val valid: Boolean)

data class ChangePasswordRequest(val oldPassword: String, val newPassword: String)
data class RecoverPasswordRequest(val email: String)
data class ResetPasswordRequest(val token: String, val newPassword: String)

data class UpdateProfileRequest(
    val firstName: String? = null,
    val lastName: String? = null,
    val phone: String? = null,
    val address: String? = null,
    val region: String? = null,
    val city: String? = null
)

data class MessageResponse(val message: String, val error: String? = null)

data class ProductDto(
    val id: Int,
    val code: String? = null,
    val name: String,
    val description: String,
    val price: Double,
    val stock: Int,
    val image: String,
    val categoryId: Int,
    val category: String? = null,
    val featured: Boolean = false,
    val active: Boolean? = true,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    
    val originalPrice: Double? = null,
    val discountedPrice: Double? = null,
    val discountPercentage: Double? = null,
    val activeDealId: Int? = null,
    val activeDealName: String? = null
)

data class CategoryDto(
    val id: Int,
    val nombre: String,
    val descripcion: String? = null
)


data class CartResponse(
    val items: List<CartItemDto>
)

data class CartItemDto(
    val id: String,
    val productId: Int,
    val productName: String,
    val productImage: String,
    val productSku: String? = null,
    val price: Double,
    val quantity: Int,
    val availableStock: Int,
    
    val originalPrice: Double? = null,
    val discountedPrice: Double? = null,
    val discountPercentage: Double? = null,
    val activeDealId: Int? = null,
    val activeDealName: String? = null
)

data class AddToCartRequest(
    val productId: Int,
    val quantity: Int
)

data class UpdateCartItemRequest(
    val quantity: Int
)


data class InitiatePaymentRequest(
    val userId: Int,
    val userEmail: String,
    val totalAmount: Double,
    val paymentMethod: String,
    val shippingAddress: String,
    val shippingCity: String,
    val shippingPhone: String,
    val products: List<PaymentProductDto>
)

data class PaymentProductDto(
    val id: Int,
    val code: String,
    val name: String,
    val price: Double,
    val quantity: Int
)

data class ConfirmPaymentRequest(
    val cardNumber: String,
    val cardholderName: String,
    val expirationMonth: String,
    val expirationYear: String,
    val cvv: String,
    val cardType: String
)

data class PaymentResponse(
    val id: String? = null,
    val paymentId: String? = null,
    val status: String? = null,
    val message: String? = null,
    val paymentToken: String? = null,
    val totalAmount: Double? = null,
    val total: Double? = null,
    val userEmail: String? = null,
    val email: String? = null,
    val paymentMethod: String? = null,
    val metodoPago: String? = null,
    val date: String? = null,
    val createdAt: String? = null,
    val items: List<PaymentProductDto>? = null,
    val products: List<PaymentProductDto>? = null
)


data class VerifyEmailRequest(val code: String)
data class RequestVerificationRequest(val username: String)


data class ForgotPasswordRequest(val email: String)
data class ResetPasswordWithTokenRequest(val token: String, val newPassword: String)


data class RegisterDeviceRequest(
    @SerializedName("fcmToken") val fcmToken: String,
    @SerializedName("platform") val platform: String
)
