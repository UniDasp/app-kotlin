package com.example.navitest.api

import com.example.navitest.model.Product
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
class ProductsRepositoryTest {

    private lateinit var repository: ProductsRepository
    private lateinit var mockApiService: ApiService

    private val sampleProductDto = ProductDto(
        id = 1,
        code = "PS5001",
        name = "PlayStation 5",
        description = "Consola de videojuegos",
        price = 500000.0,
        stock = 10,
        image = "ps5.jpg",
        categoryId = 1,
        category = "Consolas",
        featured = true,
        active = true
    )

    private val sampleProductsDto = listOf(
        sampleProductDto,
        ProductDto(
            id = 2,
            code = "XBOX001",
            name = "Xbox Series X",
            description = "Consola Microsoft",
            price = 450000.0,
            stock = 5,
            image = "xbox.jpg",
            categoryId = 1,
            category = "Consolas",
            featured = false,
            active = true
        )
    )

    private val sampleCategoriesDto = listOf(
        CategoryDto(id = 1, nombre = "Consolas", descripcion = "Consolas de videojuegos"),
        CategoryDto(id = 2, nombre = "Juegos", descripcion = "Videojuegos")
    )

    @Before
    fun setup() {
        mockApiService = mockk()
        
        
        mockkObject(RetrofitClient)
        every { RetrofitClient.apiService } returns mockApiService
        
        repository = ProductsRepository()
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `getActiveProducts retorna lista de productos exitosamente`() = runTest {
        
        coEvery { mockApiService.listActiveProducts() } returns Response.success(sampleProductsDto)
        
        
        val result = repository.getActiveProducts()
        
        
        assertTrue(result.isSuccess)
        val products = result.getOrNull()
        assertNotNull(products)
        assertEquals(2, products?.size)
        assertEquals("PlayStation 5", products?.get(0)?.name)
    }

    @Test
    fun `getActiveProducts retorna error cuando falla la peticion`() = runTest {
        
        coEvery { mockApiService.listActiveProducts() } returns Response.error(
            404,
            "Not found".toResponseBody()
        )
        
        
        val result = repository.getActiveProducts()
        
        
        assertTrue(result.isFailure)
        assertNotNull(result.exceptionOrNull())
    }

    @Test
    fun `getAllProducts retorna todos los productos`() = runTest {
        
        coEvery { mockApiService.listProducts() } returns Response.success(sampleProductsDto)
        
        
        val result = repository.getAllProducts()
        
        
        assertTrue(result.isSuccess)
        assertEquals(2, result.getOrNull()?.size)
    }

    @Test
    fun `getProductById retorna producto especifico`() = runTest {
        
        val productId = 1
        coEvery { mockApiService.getProductById(productId) } returns Response.success(sampleProductDto)
        
        
        val result = repository.getProductById(productId)
        
        
        assertTrue(result.isSuccess)
        val product = result.getOrNull()
        assertNotNull(product)
        assertEquals(1, product?.id)
        assertEquals("PlayStation 5", product?.name)
    }

    @Test
    fun `getProductById retorna error cuando no existe el producto`() = runTest {
        
        val productId = 999
        coEvery { mockApiService.getProductById(productId) } returns Response.error(
            404,
            "Not found".toResponseBody()
        )
        
        
        val result = repository.getProductById(productId)
        
        
        assertTrue(result.isFailure)
    }

    @Test
    fun `searchProducts retorna resultados de busqueda`() = runTest {
        
        val query = "PlayStation"
        val searchResults = listOf(sampleProductDto)
        coEvery { mockApiService.searchProducts(query) } returns Response.success(searchResults)
        
        
        val result = repository.searchProducts(query)
        
        
        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrNull()?.size)
        assertTrue(result.getOrNull()?.get(0)?.name?.contains("PlayStation") ?: false)
    }

    @Test
    fun `searchProducts retorna error cuando falla`() = runTest {
        
        val query = "test"
        coEvery { mockApiService.searchProducts(query) } returns Response.error(
            500,
            "Server error".toResponseBody()
        )
        
        
        val result = repository.searchProducts(query)
        
        
        assertTrue(result.isFailure)
    }

    @Test
    fun `getProductsByCategory retorna productos filtrados por categoria`() = runTest {
        
        val categoryId = 1
        coEvery { mockApiService.getProductsByCategory(categoryId) } returns Response.success(sampleProductsDto)
        
        
        val result = repository.getProductsByCategory(categoryId)
        
        
        assertTrue(result.isSuccess)
        assertEquals(2, result.getOrNull()?.size)
    }

    @Test
    fun `getProductsByCategory retorna error cuando falla`() = runTest {
        
        val categoryId = 999
        coEvery { mockApiService.getProductsByCategory(categoryId) } returns Response.error(
            404,
            "Not found".toResponseBody()
        )
        
        
        val result = repository.getProductsByCategory(categoryId)
        
        
        assertTrue(result.isFailure)
    }

    @Test
    fun `getCategories retorna lista de categorias`() = runTest {
        
        coEvery { mockApiService.listCategories() } returns Response.success(sampleCategoriesDto)
        
        
        val result = repository.getCategories()
        
        
        assertTrue(result.isSuccess)
        val categories = result.getOrNull()
        assertNotNull(categories)
        assertEquals(2, categories?.size)
        assertEquals("Consolas", categories?.get(0)?.nombre)
    }

    @Test
    fun `getCategories usa cache en segunda llamada`() = runTest {
        
        coEvery { mockApiService.listCategories() } returns Response.success(sampleCategoriesDto)
        
        
        val result1 = repository.getCategories()
        
        val result2 = repository.getCategories()
        
        
        assertTrue(result1.isSuccess)
        assertTrue(result2.isSuccess)
        
        coVerify(exactly = 1) { mockApiService.listCategories() }
    }

    @Test
    fun `getCategories retorna error cuando falla la peticion`() = runTest {
        
        coEvery { mockApiService.listCategories() } returns Response.error(
            500,
            "Server error".toResponseBody()
        )
        
        
        val result = repository.getCategories()
        
        
        assertTrue(result.isFailure)
    }

    @Test
    fun `ProductDto se convierte correctamente a Product`() = runTest {
        
        coEvery { mockApiService.getProductById(1) } returns Response.success(sampleProductDto)
        
        
        val result = repository.getProductById(1)
        
        
        assertTrue(result.isSuccess)
        val product = result.getOrNull()
        assertNotNull(product)
        assertEquals(sampleProductDto.id, product?.id)
        assertEquals(sampleProductDto.name, product?.name)
        assertEquals(sampleProductDto.price, product?.price, 0.01)
        assertEquals(sampleProductDto.stock, product?.stock)
    }

    @Test
    fun `manejo de excepciones en getActiveProducts`() = runTest {
        
        coEvery { mockApiService.listActiveProducts() } throws Exception("Network error")
        
        
        val result = repository.getActiveProducts()
        
        
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()?.message?.contains("Network error") ?: false)
    }

    @Test
    fun `manejo de excepciones en searchProducts`() = runTest {
        
        coEvery { mockApiService.searchProducts(any()) } throws Exception("Connection timeout")
        
        
        val result = repository.searchProducts("test")
        
        
        assertTrue(result.isFailure)
    }
}
