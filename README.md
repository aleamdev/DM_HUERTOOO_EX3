# 🌱 HuertoHogar — Aplicación Móvil + Backend Spring Boot
**Evaluación Parcial DSY1105**  
**Institución:** Duoc UC  
**Integrante:** Alejandro Améstica

Aplicación Android desarrollada con **Jetpack Compose**, conectada a un **backend real en Spring Boot**, con **base de datos MySQL**, autenticación con **JWT**, consumo de una **API externa (OpenWeather)** y empaquetada en un **APK firmado** para ejecución en dispositivo físico.

---

# 🚀 1) Resumen del Proyecto

HuertoHogar es una app móvil tipo mini-ecommerce que permite:

- Crear y autenticar usuarios (**login / register**)
- Ver un catálogo real de productos cargados desde un backend
- Ver detalle de productos desde el backend
- Administrar un **carrito de compras**, agregando y quitando ítems
- Realizar **checkout**
- Obtener ubicación actual del usuario
- Consultar el **clima real** según lat/lon (API externa)
- Utilizar cámara (preview)
- Mantener sesión usando DataStore
- Mostrar productos almacenados en Room como caché local

---

# 🏗 2) Arquitectura General

### **Componentes principales**
- **Android App** (Jetpack Compose + MVVM)
- **Backend en Spring Boot** (monolito modular)
- **Base de datos MySQL**
- **API externa:** OpenWeather
- **Comunicación:** Retrofit + JSON

### **Flujo general**
```
App → Retrofit → Backend Spring Boot → MySQL
App → Retrofit → OpenWeather (datos de clima)
```

---

# 📱 3) Funcionalidades de la App

### **Autenticación**
- Registro de usuario  
- Login mediante correo y contraseña  
- Generación y almacenamiento de **JWT**  
- Modo invitado

### **Catálogo**
- Lista de productos reales desde `/productos`
- Búsqueda por nombre/SKU
- Filtro por categoría
- Detalle de producto con imagen, descripción, categoría y precio

### **Carrito**
- Ver carrito `/carrito`
- Agregar item `/carrito/items`
- Eliminar item `/carrito/items/{id}`
- Checkout con cálculo total
- Todo gestionado desde **Spring + MySQL**

### **Ubicación y Clima**
- Permisos de ubicación (ACCESS_FINE_LOCATION)
- Obtención de lat/lon con FusedLocationProvider
- Consumo de OpenWeather:
```
GET /data/2.5/weather?lat={lat}&lon={lon}&appid=KEY
```

### **Cámara**
- Preview con `TakePicturePreview()`

### **Sesión y almacén local**
- **DataStore:** email, token  
- **Room:** tabla local de productos

---

# 🖥 4) Backend Spring Boot

### **Arquitectura**
- Monolito modular con:
  - `auth` (login/register)
  - `productos`
  - `carrito`
- Capas:
  - Controller
  - Service
  - Repository
  - Entity + JPA

### **Base de datos**
MySQL con tablas:
- `usuarios`
- `productos`
- `carrito`
- `carrito_items`
- `categoria`

### **Configuración**
```
spring.datasource.url=jdbc:mysql://localhost:3306/huertohogar
spring.datasource.username=test
spring.datasource.password=test
server.address=0.0.0.0
server.port=8080
jwt.secret=XXXXX
```

---

# 🔌 5) Integración App ↔ Backend

Retrofit configurado con IP local:
```kotlin
private const val BASE_URL = "http://192.168.X.X:8080/"
```

Cada endpoint retorna DTOs mapeados automáticamente.

La app almacena el token JWT en DataStore y lo envía en:
```
Authorization: Bearer TOKEN
```

---

# 📂 6) Estructura del Proyecto (Android)

```
app/
├─ data/
│  ├─ remote/ (Retrofit, DTOs, APIs)
│  ├─ local/ (Room)
│  ├─ datastore/ (DataStore)
├─ ui/
│  ├─ screens/ (auth, home, catalog, detail, cart, device)
│  ├─ components/
│  ├─ session/
│  └─ theme/
├─ navigation/
└─ MainActivity.kt
```

---

# 🧪 7) Ejecución del Proyecto

## **A) Backend**
1. Instalar MySQL  
2. Crear BD `huertohogar`  
3. Configurar usuario y clave  
4. Ejecutar Spring Boot:

```
mvn spring-boot:run
```

Backend quedará disponible en:  
👉 `http://<tu-ip-local>:8080`

## **B) App Android**
1. Abrir en Android Studio (Koala o superior)  
2. Sincronizar Gradle  
3. Reemplazar IP en `ApiClient.kt`  
4. Ejecutar en:
   - Emulador  
   - Dispositivo físico (recomendado)

## **C) APK firmada**
Se generó mediante:  
**Build → Generate Signed APK → release**

---

# 🔧 8) Permisos (AndroidManifest)

```
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
<uses-permission android:name="android.permission.CAMERA"/>
<application android:usesCleartextTraffic="true"/>
```

---

# 📦 9) Dependencias principales

- Jetpack Compose BOM  
- Material 3  
- Navigation Compose  
- Retrofit + Gson  
- Coil-Compose  
- DataStore  
- Room  
- Play Services Location  
- JDK 17  

---

# ⭐ 10) Conclusiones

- La app móvil se integra correctamente con un backend real.  
- La base de datos MySQL almacena productos, usuarios y carrito.  
- El sistema completo demuestra comunicación REST full-duplex.  
- El proyecto incluye autenticación JWT, caché local, API externa y sensores.  
- La APK firmada permite la ejecución en teléfono físico.

---

# 📌 11) Mejoras futuras

- Implementar módulo de pedidos  
- Agregar pasarela de pagos  
- Push notifications  
- Persistencia completa local del carrito  
- Animaciones avanzadas  
- Modo offline

---

# 🧑‍💻 12) Nota del Autor

Este proyecto demuestra dominio de:
- Android moderno (Compose + MVVM)  
- Spring Boot profesional  
- Integración de API REST  
- Manejo de BD relacional  
- Librerías clave como Retrofit, DataStore y Room

# Documentacion creada por copilot
