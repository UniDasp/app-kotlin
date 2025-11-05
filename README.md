# Levelup Gamer 🎮

Aplicación Android de comercio electrónico desarrollada en Kotlin con Jetpack Compose, especializada en la venta de productos gamer, consolas, accesorios y juegos de mesa.

## 👥 Equipo de Desarrollo
                                                            
- **Pablo Terrazas**
- **Diego Sandoval**

## 📱 Descripción del Proyecto

Levelup Gamer es una aplicación móvil para Android que ofrece una experiencia de compra completa para gamers. La app permite explorar productos, agregar items al carrito de compras, ver productos destacados y gestionar las preferencias del usuario.

## ✨ Funcionalidades Implementadas

### 🏠 Página Principal (Home)
- Pantalla de bienvenida con banner promocional (20% descuento con correo DuocUC)
- Sección de "Vistos Recientemente" que registra los productos que el usuario ha revisado
- Categorías populares con imágenes:
  - Consolas
  - Accesorios
  - Computadores Gamers
  - Juegos de Mesa
- Productos destacados en carrusel horizontal
- Diálogo detallado de productos con imagen, descripción y precio

### 🛒 Carrito de Compras
- Visualización de productos agregados al carrito
- Cálculo automático del total de la compra
- Sistema de gestión de cantidad de productos
- Acceso rápido desde el icono en la barra superior

### 🛍️ Catálogo de Productos
- Lista completa de productos disponibles organizados por categorías:
  - Juegos de Mesa (Catan, Carcassonne)
  - Accesorios (Controladores, Auriculares)
  - Consolas (PlayStation 5)
  - Computadores Gamers (PC ASUS ROG Strix)
  - Sillas Gamers
  - Periféricos (Mouse, Mousepad)
  - Merchandising (Poleras Personalizadas)
- Tarjetas de producto con imagen, nombre y precio formateado en CLP
- Sistema de búsqueda y filtrado

### 🔔 Notificaciones
- Página dedicada para notificaciones del usuario
- Alertas sobre ofertas y novedades

### 👤 Cuenta de Usuario
- Gestión de perfil de usuario
- Configuración de preferencias

### 🎨 Características Técnicas
- **Material Design 3**: Interfaz moderna con Material You
- **Jetpack Compose**: UI declarativa y reactiva
- **Navegación por pestañas**: Bottom Navigation Bar con 4 secciones principales
- **Formateo de moneda**: Precios mostrados en formato chileno (CLP)
- **Carga de imágenes**: Integración con Coil para carga asíncrona de imágenes
- **Arquitectura MVVM**: Separación de lógica de negocio y presentación
- **Repository Pattern**: Gestión centralizada de datos (ProductRepository, CartRepository, RecentRepository)

## 🛠️ Tecnologías Utilizadas

- **Lenguaje**: Kotlin 2.0.21
- **Framework UI**: Jetpack Compose
- **Android SDK**: 
  - Min SDK: 24 (Android 7.0)
  - Target SDK: 36
  - Compile SDK: 36
- **Java Version**: Java 11
- **Build System**: Gradle 8.2.2 con Kotlin DSL
- **Librerías principales**:
  - AndroidX Core KTX
  - AndroidX Lifecycle Runtime KTX
  - Compose BOM 2024.09.00
  - Material 3
  - Coil 2.4.0 (Carga de imágenes)
  - Lifecycle ViewModel Compose 2.7.0

## 📋 Requisitos Previos

- **Android Studio**: Hedgehog (2023.1.1) o superior
- **JDK**: Java 11 o superior
- **Sistema Operativo**: Windows, macOS o Linux
- **Dispositivo/Emulador**: Android 7.0 (API 24) o superior

## 🚀 Pasos para Ejecutar el Proyecto

### 1️⃣ Clonar el Repositorio

```bash
git clone https://github.com/UniDasp/app-kotlin.git
cd app-kotlin
```

### 2️⃣ Abrir en Android Studio

1. Abre **Android Studio**
2. Selecciona `File` → `Open`
3. Navega hasta la carpeta del proyecto y selecciónala
4. Espera a que Gradle sincronice las dependencias automáticamente

### 3️⃣ Configurar el Dispositivo

**Opción A: Usar un Emulador**
1. En Android Studio, ve a `Tools` → `Device Manager`
2. Crea un nuevo dispositivo virtual (AVD) si no tienes uno
3. Recomendado: Pixel 5 o superior con Android 11+
4. Inicia el emulador

**Opción B: Usar un Dispositivo Físico**
1. Habilita las **Opciones de Desarrollador** en tu dispositivo Android
2. Activa la **Depuración USB**
3. Conecta tu dispositivo mediante USB
4. Autoriza la conexión cuando se te solicite

### 4️⃣ Compilar y Ejecutar

**Método 1: Desde Android Studio**
1. Asegúrate de que el dispositivo/emulador esté seleccionado en la barra superior
2. Haz clic en el botón **Run** ▶️ (o presiona `Shift + F10`)
3. Espera a que la app se compile e instale

**Método 2: Desde la Terminal**

En Windows PowerShell:
```powershell
.\gradlew.bat assembleDebug
.\gradlew.bat installDebug
```

En Linux/macOS:
```bash
./gradlew assembleDebug
./gradlew installDebug
```

### 5️⃣ Explorar la Aplicación

1. La app se abrirá automáticamente en tu dispositivo
2. Navega por las diferentes secciones usando la barra de navegación inferior:
   - **Home**: Explora productos destacados y categorías
   - **Productos**: Navega el catálogo completo
   - **Notificaciones**: Revisa alertas y novedades
   - **Cuenta**: Gestiona tu perfil
3. Toca cualquier producto para ver sus detalles
4. Usa el ícono de carrito en la barra superior para ver tus items seleccionados

## 📂 Estructura del Proyecto

```
app-kotlin/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/navitest/
│   │   │   │   ├── model/           # Modelos de datos
│   │   │   │   │   ├── Product.kt
│   │   │   │   │   ├── Cart.kt
│   │   │   │   │   ├── User.kt
│   │   │   │   │   └── RecentRepository.kt
│   │   │   │   ├── pages/           # Pantallas de la app
│   │   │   │   │   ├── HomePage.kt
│   │   │   │   │   ├── ProductsPage.kt
│   │   │   │   │   ├── CartPage.kt
│   │   │   │   │   ├── NotificationsPage.kt
│   │   │   │   │   ├── AccountPage.kt
│   │   │   │   │   └── LoginScreen.kt
│   │   │   │   ├── viewmodel/       # ViewModels
│   │   │   │   │   └── LoginViewModel.kt
│   │   │   │   ├── ui/theme/        # Tema y diseño
│   │   │   │   │   ├── Color.kt
│   │   │   │   │   ├── Theme.kt
│   │   │   │   │   └── Type.kt
│   │   │   │   ├── utils/           # Utilidades
│   │   │   │   │   └── NumberFormat.kt
│   │   │   │   ├── MainActivity.kt
│   │   │   │   ├── PantallaInicial.kt
│   │   │   │   └── ItemsNav.kt
│   │   │   └── AndroidManifest.xml
│   │   └── test/                    # Tests unitarios
│   └── build.gradle.kts
├── gradle/
│   └── libs.versions.toml           # Gestión de versiones
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

## 🎨 Capturas de Pantalla

_Próximamente se agregarán capturas de pantalla de la aplicación en funcionamiento._

## 🔧 Solución de Problemas

### Error de Sincronización de Gradle
```powershell
.\gradlew.bat clean
.\gradlew.bat build
```

### La app no se instala en el dispositivo
- Verifica que la depuración USB esté habilitada
- Revisa que el dispositivo tenga espacio suficiente
- Intenta reiniciar Android Studio y el dispositivo

### Errores de compilación
- Asegúrate de tener instalado JDK 11 o superior
- Verifica que Android Studio esté actualizado
- Limpia el proyecto: `Build` → `Clean Project`
- Reconstruye: `Build` → `Rebuild Project`

## 📄 Licencia

Este proyecto es un trabajo académico desarrollado para fines educativos.

## 📞 Contacto

Para consultas o sugerencias sobre el proyecto, contacta a:
- Pablo Terrazas
- Diego Sandoval

---

**Desarrollado con ❤️ por estudiantes de DuocUC** 🎓
