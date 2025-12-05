package com.example.navitest.api

import com.example.navitest.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepository {
    private val api = RetrofitClient.apiService

    suspend fun login(username: String, password: String): Result<LoginResponse> = withContext(Dispatchers.IO) {
        try {
            val response = api.login(LoginRequest(username, password))
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Login failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(
        username: String,
        password: String,
        email: String,
        firstName: String,
        lastName: String,
        phone: String? = null,
        address: String? = null,
        region: String? = null
    ): Result<RegisterResponse> = withContext(Dispatchers.IO) {
        try {
            val response = api.register(
                RegisterRequest(
                    username = username,
                    password = password,
                    email = email,
                    firstName = firstName,
                    lastName = lastName,
                    phone = phone,
                    address = address,
                    region = region
                )
            )
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Registration failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getCurrentUser(token: String): Result<User> = withContext(Dispatchers.IO) {
        try {
            val authToken = if (token.startsWith("Bearer ")) token else "Bearer $token"
            val response = api.getCurrentUser(authToken)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to get user: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun changePassword(token: String, oldPassword: String, newPassword: String): Result<MessageResponse> = 
        withContext(Dispatchers.IO) {
            try {
                val authToken = if (token.startsWith("Bearer ")) token else "Bearer $token"
                val response = api.changePassword(authToken, ChangePasswordRequest(oldPassword, newPassword))
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception("Password change failed: ${response.message()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    suspend fun validateToken(token: String): Result<ValidateResponse> = withContext(Dispatchers.IO) {
        try {
            val authToken = if (token.startsWith("Bearer ")) token else "Bearer $token"
            val response = api.validate(authToken)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Token validation failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateProfile(
        token: String,
        userId: Int,
        user: User
    ): Result<User> = withContext(Dispatchers.IO) {
        try {
            val authToken = if (token.startsWith("Bearer ")) token else "Bearer $token"
            val response = api.updateUser(authToken, userId, user)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Profile update failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    
    suspend fun verifyEmail(token: String, code: String): Result<MessageResponse> = 
        withContext(Dispatchers.IO) {
            try {
                val authToken = if (token.startsWith("Bearer ")) token else "Bearer $token"
                val response = api.verifyEmail(authToken, VerifyEmailRequest(code))
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception("Email verification failed: ${response.message()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    
    suspend fun resendVerificationCode(token: String): Result<MessageResponse> = 
        withContext(Dispatchers.IO) {
            try {
                val authToken = if (token.startsWith("Bearer ")) token else "Bearer $token"
                val response = api.resendVerificationCode(authToken)
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception("Resend verification failed: ${response.message()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    
    suspend fun requestVerificationByUsername(username: String): Result<MessageResponse> = 
        withContext(Dispatchers.IO) {
            try {
                val response = api.requestVerificationByUsername(RequestVerificationRequest(username))
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception("Request verification failed: ${response.message()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    
    
    suspend fun forgotPassword(email: String): Result<MessageResponse> = 
        withContext(Dispatchers.IO) {
            try {
                val response = api.forgotPassword(ForgotPasswordRequest(email))
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception("Forgot password failed: ${response.message()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    
    suspend fun resetPasswordWithToken(token: String, newPassword: String): Result<MessageResponse> = 
        withContext(Dispatchers.IO) {
            try {
                val response = api.resetPasswordWithToken(ResetPasswordWithTokenRequest(token, newPassword))
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception("Password reset failed: ${response.message()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
}
