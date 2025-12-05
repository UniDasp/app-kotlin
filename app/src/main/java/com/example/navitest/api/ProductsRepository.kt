package com.example.navitest.api

import com.example.navitest.model.Category
import com.example.navitest.model.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductsRepository {
    private val api = RetrofitClient.apiService
    
    
    private var categoriesCache: List<Category>? = null

    suspend fun getActiveProducts(): Result<List<Product>> = withContext(Dispatchers.IO) {
        try {
            val response = api.listActiveProducts()
            if (response.isSuccessful && response.body() != null) {
                val products = response.body()!!.map { it.toProduct() }
                Result.success(products)
            } else {
                Result.failure(Exception("Failed to fetch products: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAllProducts(): Result<List<Product>> = withContext(Dispatchers.IO) {
        try {
            val response = api.listProducts()
            if (response.isSuccessful && response.body() != null) {
                val products = response.body()!!.map { it.toProduct() }
                Result.success(products)
            } else {
                Result.failure(Exception("Failed to fetch products: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getProductById(id: Int): Result<Product> = withContext(Dispatchers.IO) {
        try {
            val response = api.getProductById(id)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.toProduct())
            } else {
                Result.failure(Exception("Product not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun searchProducts(query: String): Result<List<Product>> = withContext(Dispatchers.IO) {
        try {
            val response = api.searchProducts(query)
            if (response.isSuccessful && response.body() != null) {
                val products = response.body()!!.map { it.toProduct() }
                Result.success(products)
            } else {
                Result.failure(Exception("Search failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getProductsByCategory(categoryId: Int): Result<List<Product>> = withContext(Dispatchers.IO) {
        try {
            val response = api.getProductsByCategory(categoryId)
            if (response.isSuccessful && response.body() != null) {
                val products = response.body()!!.map { it.toProduct() }
                Result.success(products)
            } else {
                Result.failure(Exception("Failed to fetch category products: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getCategories(): Result<List<Category>> = withContext(Dispatchers.IO) {
        try {
            
            categoriesCache?.let { return@withContext Result.success(it) }
            
            val response = api.listCategories()
            if (response.isSuccessful && response.body() != null) {
                val categories = response.body()!!.map { 
                    Category(
                        id = it.id,
                        nombre = it.nombre,
                        descripcion = it.descripcion
                    )
                }
                categoriesCache = categories
                Result.success(categories)
            } else {
                Result.failure(Exception("Failed to fetch categories: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun ProductDto.toProduct(): Product {
        val category = categoriesCache?.find { it.id == this.categoryId }
        return Product(
            id = this.id,
            code = this.code,
            name = this.name,
            description = this.description,
            price = this.price,
            image = this.image,
            categoryId = this.categoryId,
            category = this.category ?: category?.nombre,
            categoryNameAlt = null,
            featured = this.featured,
            stock = this.stock,
            originalPrice = this.originalPrice,
            discountedPrice = this.discountedPrice,
            discountPercentage = this.discountPercentage,
            activeDealId = this.activeDealId,
            activeDealName = this.activeDealName,
            active = this.active ?: true,
            createdAt = this.createdAt,
            updatedAt = this.updatedAt
        )
    }
}
