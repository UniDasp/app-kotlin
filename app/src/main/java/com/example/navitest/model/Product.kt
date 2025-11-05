package com.example.navitest.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class Product(
    val id: String,
    val code: String,
    val name: String,
    val price: Int,
    val category: ProductCategory,
    val description: String,
    val image: String,
    val featured: Boolean = false
)

enum class ProductCategory(
    val displayName: String, 
    val color: Color
) {
    BOARD_GAMES("Juegos de Mesa", Color(0xFFE65100)),
    ACCESSORIES("Accesorios", Color(0xFF2196F3)),
    CONSOLES("Consolas", Color(0xFF4CAF50)),
    GAMING_PC("Computadores Gamers", Color(0xFF9C27B0)),
    GAMING_CHAIRS("Sillas Gamers", Color(0xFFFF5252)),
    MOUSE("Mouse", Color(0xFF3F51B5)),
    MOUSEPADS("Mousepad", Color(0xFF607D8B)),
    CUSTOM_SHIRTS("Poleras Personalizadas", Color(0xFFFF9800))
}

// Los mismos prods que la pagina xd
object ProductRepository {
    val products = listOf(
        Product(
            id = "JM001",
            code = "JM001",
            name = "Catan",
            price = 29990,
            category = ProductCategory.BOARD_GAMES,
            image = "https://i.imgur.com/Azw0XR8.jpeg",
            description = "Un clásico juego de estrategia donde los jugadores compiten por colonizar y expandirse en la isla de Catan. Ideal para 3-4 jugadores y perfecto para noches de juego en familia o con amigos."
        ),
        Product(
            id = "JM002",
            code = "JM002",
            name = "Carcassonne",
            price = 24990,
            category = ProductCategory.BOARD_GAMES,
            image = "https://i.imgur.com/sQWaneo.jpeg",
            description = "Un juego de colocación de fichas donde los jugadores construyen el paisaje alrededor de la fortaleza medieval de Carcassonne. Ideal para 2-5 jugadores y fácil de aprender."
        ),
        Product(
            id = "AC001",
            code = "AC001",
            name = "Controlador Inalámbrico Xbox Series X",
            price = 59990,
            category = ProductCategory.ACCESSORIES,
            image = "https://i.imgur.com/9M7hnZA.jpeg",
            description = "Ofrece una experiencia de juego cómoda con botones mapeables y una respuesta táctil mejorada. Compatible con consolas Xbox y PC."
        ),
        Product(
            id = "AC002",
            code = "AC002",
            name = "Auriculares Gamer HyperX Cloud II",
            price = 79990,
            category = ProductCategory.ACCESSORIES,
            image = "https://i.imgur.com/NB1Ulfk.png",
            description = "Proporcionan un sonido envolvente de calidad con un micrófono desmontable y almohadillas de espuma viscoelástica para mayor comodidad durante largas sesiones de juego."
        ),
        Product(
            id = "CO001",
            code = "CO001",
            name = "PlayStation 5",
            price = 549990,
            category = ProductCategory.CONSOLES,
            image = "https://i.imgur.com/YYOfcW8.png",
            description = "La consola de última generación de Sony, que ofrece gráficos impresionantes y tiempos de carga ultrarrápidos para una experiencia de juego inmersiva.",
            featured = true
        ),
        Product(
            id = "CG001",
            code = "CG001",
            name = "PC Gamer ASUS ROG Strix",
            price = 1299990,
            category = ProductCategory.GAMING_PC,
            image = "https://i.imgur.com/FxQ3ux3.png",
            description = "Un potente equipo diseñado para los gamers más exigentes, equipado con los últimos componentes para ofrecer un rendimiento excepcional en cualquier juego.",
            featured = true
        ),
        Product(
            id = "SG001",
            code = "SG001",
            name = "Silla Gamer Secretlab Titan",
            price = 349990,
            category = ProductCategory.GAMING_CHAIRS,
            image = "https://i.imgur.com/U2T8uoA.jpeg",
            description = "Diseñada para el máximo confort, esta silla ofrece un soporte ergonómico y personalización ajustable para sesiones de juego prolongadas."
        ),
        Product(
            id = "MS001",
            code = "MS001",
            name = "Mouse Gamer Logitech G502 HERO",
            price = 49990,
            category = ProductCategory.MOUSE,
            image = "https://i.imgur.com/FlI4DDD.jpeg",
            description = "Con sensor de alta precisión y botones personalizables, este mouse es ideal para gamers que buscan un control preciso y personalización."
        ),
        Product(
            id = "MP001",
            code = "MP001",
            name = "Mousepad Razer Goliathus Extended Chroma",
            price = 29990,
            category = ProductCategory.MOUSEPADS,
            image = "https://i.imgur.com/QEwsZ3x.png",
            description = "Ofrece un área de juego amplia con iluminación RGB personalizable, asegurando una superficie suave y uniforme para el movimiento del mouse."
        ),
        Product(
            id = "PP001",
            code = "PP001",
            name = "Polera Gamer Personalizada 'Level-Up'",
            price = 14990,
            category = ProductCategory.CUSTOM_SHIRTS,
            image = "https://i.imgur.com/TtfuMsF.png",
            description = "Una camiseta cómoda y estilizada, con nuestro logo de Powerful Gamer, para que muestres tu pasión por los videojuegos."
        )
    )
}