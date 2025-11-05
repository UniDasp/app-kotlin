package com.example.navitest.model

data class User(
    val id: Int,
    val username: String,
    val email: String,
    val role: UserRole,
    val password: String
)

enum class UserRole {
    USER,
    ADMIN
}

object UserRepository {
    val users = listOf(
        User(
            id = 1,
            username = "diego",
            email = "diego@levelup.com",
            role = UserRole.USER,
            password = "password123"
        ),
        User(
            id = 2,
            username = "pablo",
            email = "pablo@levelup.com",
            role = UserRole.USER,
            password = "password123"
        ),
        User(
            id = 3,
            username = "diego_admin",
            email = "diego.admin@levelup.com",
            role = UserRole.ADMIN,
            password = "adminpass"
        ),
        User(
            id = 4,
            username = "pablo_admin",
            email = "pablo.admin@levelup.com",
            role = UserRole.ADMIN,
            password = "adminpass"
        ),
        User(
            id = 5,
            username = "diego_duoc",
            email = "diego@duocuc.cl",
            role = UserRole.USER,
            password = "duocpass"
        ),
        User(
            id = 6,
            username = "pablo_duoc",
            email = "pablo@duocuc.cl",
            role = UserRole.USER,
            password = "duocpass"
        )
    )
}
