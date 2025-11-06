# Levelup Gamer 🎮

Una app Android para comprar productos gamer. Hecha con Kotlin y Jetpack Compose.

## El equipo

- **Pablo Terrazas**
- **Diego Sandoval**

## ¿Qué es esto?

Básicamente una tienda online para gamers donde puedes buscar consolas, accesorios, juegos de mesa y todo lo relacionado con gaming. La armamos para nuestro proyecto del Duoc.

## Lo que hace la app

### Página de inicio

Cuando abres la app te encuentras con:

- Un banner con 20% de descuento si tienes correo del Duoc (aprovecha mientras dure jaja)
- Los productos que viste hace rato
- Categorías para navegar más fácil (Consolas, Accesorios, PCs Gamers, Juegos de Mesa)
- Algunos productos destacados que van rotando

### Carrito

Lo típico: agregas productos, ves el total y listo. Está siempre disponible desde el ícono de arriba.

### Catálogo

Aquí está todo lo que vendemos:

- Juegos de mesa (tenemos Catan y Carcassonne por ahora)
- Accesorios tipo controles y audífonos
- La PS5 (cuando hay stock 😅)
- PCs armados para gaming
- Sillas gamers
- Periféricos
- Poleras personalizadas

Los precios están en pesos chilenos para que no te confundas.

### Otras secciones

- **Notificaciones**: Para que no te pierdas las ofertas
- **Cuenta**: Tu perfil y configuración

## Con qué lo hicimos

- Kotlin (obvio)
- Jetpack Compose para la interfaz
- Material Design 3 para que se vea bonito
- Un montón de librerías de Android que son medio complicadas de explicar

**Versiones y eso:**

- Funciona desde Android 7.0 en adelante
- Usamos Gradle 8.2.2
- Java 11

## Cómo probarlo

### 1. Clonar el repo

```bash
git clone https://github.com/UniDasp/app-kotlin.git
cd app-kotlin
```

### 2. Abrirlo en Android Studio

File → Open → seleccionas la carpeta y listo. Espera que baje todas las dependencias.

### 3. Configurar donde lo vas a correr

**Si usas el emulador:**

- Tools → Device Manager
- Créate un Pixel 5 o algo similar con Android 11+
- Dale play

**Si tienes un celular Android:**

- Activa las opciones de desarrollador (busca en Google cómo según tu celular)
- Prende la depuración USB
- Conecta el cable
- Acepta los permisos que te pida

### 4. Correr la app

Dale al botón de Run ▶️ en Android Studio (o Shift + F10).

Si prefieres la terminal:

En Windows:

```powershell
.\gradlew.bat assembleDebug
.\gradlew.bat installDebug
```

Linux/Mac:

```bash
./gradlew assembleDebug
./gradlew installDebug
```

### 5. Úsala

Navega con los íconos de abajo, toca los productos para ver detalles, agrega cosas al carrito. Nada del otro mundo.

## Si algo no funciona

**Gradle no sincroniza:**

```powershell
.\gradlew.bat clean
.\gradlew.bat build
```

**No se instala:**

- Revisa que la depuración USB esté prendida
- Mira si tienes espacio en el celular
- Reinicia todo (Android Studio y el celu)

**Errores raros al compilar:**

- Verifica que tengas JDK 11
- Actualiza Android Studio
- Build → Clean Project → Rebuild Project

## Estructura básica

```
app-kotlin/
├── app/
│   ├── src/main/java/com/example/navitest/
│   │   ├── model/          # Los datos
│   │   ├── pages/          # Las pantallas
│   │   ├── viewmodel/      # La lógica
│   │   ├── ui/theme/       # Colores y estilos
│   │   └── MainActivity.kt
│   └── build.gradle.kts
└── README.md
```

## Notas finales

Esto es un proyecto universitario, así que no esperen que sea perfecto. Lo hicimos con harto café y poco sueño 😴

Si tienen dudas o encuentran bugs, avisen nomás.

---

**Desarrollado con ❤️ por estudiantes de DuocUC** 🎓
