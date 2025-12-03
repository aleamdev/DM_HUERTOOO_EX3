# HuertoHogar — Evaluación Parcial 2 (DSY1105)

App Android hecha con **Jetpack Compose + Material 3** que simula un mini e-commerce de alimentos frescos (frutas, verduras, orgánicos y lácteos)
> **Institución:** Duoc UC  
> **Asignatura:** DSY1105  
> **Integrantes:** Alejandro Amestica
---

## 1) Qué hace la app (resumen corto)

- **Login** con validación de email y clave mínima de 6.
- **Invitado** (entra sin cuenta).
- **Home** con hero, chips de **categorías**, **destacados**, links a blog y ubicaciones.
- **Catálogo** con **búsqueda** (`q`) y **filtro por categoría** (`cat`).
- **Detalle** de producto (placeholder).
- **Cámara** (preview rápida).
- **Ubicación** (última lat/lon con FusedLocation).
- **Sesión** en local con DataStore y productos en Room.

## 2) Estado del proyecto

###  Implementado
- **Material 3**: tema `Theme.HuertoHogar` (DayNight), `TopAppBar`, `Card`, `Button`, `DropdownMenu`, `OutlinedTextField`.
- **Formularios validados**:  
  - Email con `Patterns.EMAIL_ADDRESS`.  
  - Clave mínimo **6**.  
  - Errores visibles con `AnimatedVisibility`.
- **Navegación funcional (NavHost)**:  
  `login`, `home`, `catalog?q&cat`, `detail/{productId}`, `camera`, `location`.
- **Gestión de estado **:  
  `AuthViewModel`, `SessionViewModel`, `CatalogViewModel`.
- **Almacenamiento local**:  
  - **DataStore**: correo de sesión (`EMAIL`).  
  - **Room**: tabla `products` con **seed**).
- **Recursos nativos**:  
  - **Cámara** con `ActivityResultContracts.TakePicturePreview()` (muestra miniatura).  
  - **Ubicación** con FusedLocation (método `lastLocation`).
- **Logout**: menú de cuenta (icono usuariop) → **Cerrar sesión**.
- **Búsqueda y categorías** en catálogo (filtra por nombre, sku, descripción, precio; categorías por prefijo de SKU: FR/VR/PO/PL).

###  Medianamente implementado
- **Registro de usuario**: la pantalla existe, **valida**, pero **no guarda** el usuario aún.  
  > El método `register()` está conectado a la UI pero no persiste en DataStore (hay utilidades en `SessionDataStore` para eso, faltaría llamarlas).
- **Login**: valida formato y **setea sesión**; **no** verifica contra un listado de usuarios guardados (hasta que se active el registro real).
- **Permisos runtime**: Ubicación revisa si el permiso está otorgado, pero **no solicita** permiso en tiempo de ejecución (debería pedirse si falta).
- **Animaciones**: hay **mínimas** (errores con `AnimatedVisibility` + menús desplegables). No se añadió aún app bar colapsable ni animaciones de tarjetas.

### Pendiente (aun no implementado)
- **Carrito** (persistencia, cantidades, resumen).
- **Detalle de producto** completo (solo placeholder).
- **Guardar foto en galería** / fichero (solo preview).
- **Mapa embebido** (se abre Google Maps externo).
- **Tests unitarios/instrumentados**.

## 3) Estructura principal

app/
├─ AndroidManifest.xml
├─ build.gradle.kts
├─ java/com/dewis/huertohogar/
│ ├─ data/
│ │ ├─ datastore/SessionDataStore.kt # sesión (EMAIL) y helpers
│ │ └─ local/
│ │ ├─ AppDatabase.kt # Room DB
│ │ ├─ Product.kt # @Entity
│ │ └─ ProductDao.kt # @Dao
│ ├─ navigation/AppNavHost.kt # rutas + argumentos (q, cat)
│ ├─ ui/
│ │ ├─ components/AppTopBar.kt # cuenta, categorías, búsqueda
│ │ ├─ screens/
│ │ │ ├─ auth/ (LoginScreen, AuthViewModel)
│ │ │ ├─ home/ (HomeScreen)
│ │ │ ├─ catalog/ (CatalogScreen, CatalogViewModel)
│ │ │ ├─ detail/ (ProductDetailScreen)
│ │ │ └─ device/ (CameraScreen, LocationScreen)
│ │ ├─ session/SessionViewModel.kt
│ │ └─ theme/ (AppTheme, Typography)
│ └─ MainActivity.kt
└─ res/values/ (themes.xml, strings.xml, colors.xml)

## 4) Cómo correr

1. Abrir en **Android Studio Ladybug/Koala o superior**.  
2. Sincronizar Gradle.  
3. Ejecutar en emulador **Pixel** (Android 8.0+) o dispositivo físico.  
4. Si sale *“waiting for all target devices to come online”*:  
   - AVD Manager → **Cold Boot**.  
   - Desactivar snapshots o crear un AVD nuevo (x86_64).  

**Flujo sugerido:**  
- En **Login**, probar con un email válido y clave ≥ 6 (o **Entrar como invitado**).  
- Home → ir a Catálogo con la lupa o por categorías.  
- Probar Cámara (preview) y Ubicación (si el permiso ya está otorgado).

**Permisos declarados (AndroidManifest):**

<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
<uses-permission android:name="android.permission.CAMERA"/>

## 5) Dependencias

- Compose BOM 2024.10.01, material3, navigation-compose
- DataStore 1.1.1
- Room 2.6.1 (kapt para room-compiler)
- Coil-Compose 2.6.0 (imágenes)
- Play Services Location 21.3.0
- CameraX 1.4.0
- compileSdk 35, targetSdk 35, minSdk 24, JDK 17

## 6) Autoevaluacion 
- Material 3 consistente (tema y componentes).

- Formularios validados (email y contraseña).

- Navegación entre pantallas con argumentos.

- Gestión de estado.

- Almacenamiento local (DataStore para sesión, Room para productos).

- Recursos nativos (Cámara preview y Ubicación).

-/ Animaciones: mínimas (visible en errores y menús). (Mejora planificada abajo)

## 7) Mejoras

- Registro real (DataStore): llamar a appendUser() en register() y validar userExists() en login().
- Permisos runtime: pedir FINE LOCATION y manejar denegados.
- Animaciones “visibles”
- AppBar colapsable con TopAppBarDefaults.enterAlwaysScrollBehavior()
- animateContentSize() y animateItemPlacement() en tarjetas del catálogo.
- Crossfade de imágenes con ImageRequest.Builder(...).crossfade(true).
- Carrito: entidad Room para líneas, total, y guardado.
- Detale de producto con más info + “Agregar al carrito” funcional.
- Guardar foto tomada y compartir.
- Mapa embebido o Intent con marcadores predefinidos.
- Pruebas básicas (unitarias y de UI).

## 8) Notas

No hay backend ni pagos, todo es local para demostrar los puntos de la pauta
