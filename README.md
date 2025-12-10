HuertoHogar — Aplicación Móvil + Microservicios + API Gateway

EXAMEN TRANSVERSAL DSY1105
Institución: Duoc UC
Integrante: Alejandro Améstica

Aplicación Android desarrollada con Jetpack Compose, conectada a un backend basado en microservicios independientes, orquestados mediante un API Gateway.
La app consume servicios internos (auth, catálogo, carrito), accede a sensores del dispositivo (GPS y cámara), almacena datos localmente (Room + DataStore), y además integra una API externa (OpenWeather) consultada directamente desde el cliente.
El proyecto se entrega como APK firmada, apta para instalación en dispositivo físico.

1) Resumen del Proyecto

HuertoHogar es una mini-aplicación tipo e-commerce que permite:

Crear y autenticar usuarios (login / registro / invitado)

Obtener catálogo completo de productos desde backend

Ver detalles de productos

Administrar un carrito de compras completo

Realizar checkout

Obtener ubicación real del usuario

Consultar clima actual mediante OpenWeather

Usar cámara para obtener un preview

Persistir sesión localmente mediante DataStore

Guardar productos en Room como caché

2) Arquitectura General
Componentes principales

Android App (Jetpack Compose + MVVM)

API Gateway

Microservicios independientes:

auth-service → manejo de usuarios + JWT

catalog-service → listado y detalle de productos

cart-service → carrito y checkout

Base de datos MySQL

API externa OpenWeather (consumida directamente desde la app)

Flujo de comunicación
                ┌───────────────────────────┐
                │        Android App        │
                └──────────────┬────────────┘
                               │
        ┌──────────────────────┴──────────────────────┐
        │                                             │
┌───────────────┐                          ┌──────────────────────┐
│   API Gateway  │   <───── interno ─────  │   OpenWeather API    │
└───────┬────────┘                          └──────────────────────┘
        │
        │   Enrutamiento según path:
        │   /auth → auth-service
        │   /productos → catalog-service
        │   /carrito → cart-service
        │
┌───────────────┐   ┌──────────────────┐   ┌─────────────────────┐
│ auth-service   │   │ catalog-service │   │  cart-service        │
└───────────────┘   └──────────────────┘   └─────────────────────┘
             └──────────── MySQL Database ───────────────┘

Importante:

La app NO usa el gateway para el clima.
OpenWeather se consulta directamente desde el dispositivo.

3) Funcionalidades de la App
Autenticación (auth-service)

Registro de usuario

Login

Generación y almacenamiento de JWT

Inicio como invitado

Persistencia de sesión con DataStore

Catálogo (catalog-service):

Listado de productos

Búsqueda por texto

Filtro por categoría

Detalle del producto

Capa de caché local con Room

Carrito (cart-service):

Ver carrito por usuario

Agregar ítems

Eliminar ítems

Calcular total

Checkout

Ubicación + Clima (OpenWeather):

Solicitud de permisos

GPS mediante FusedLocationProvider

Consulta directa a OpenWeather:

GET https://api.openweathermap.org/data/2.5/weather?lat=XX&lon=YY&appid=KEY

Cámara:

Preview rápido usando TakePicturePreview()

Persistencia local:

DataStore: token JWT + email

Room: productos como caché local

4) Backend basado en Microservicios
API Gateway

Se encarga de recibir todas las solicitudes y distribuirlas según el path:

Path	Microservicio destino
/auth/**	auth-service
/productos/**	catalog-service
/carrito/**	cart-service

La app solo se comunica con el gateway, nunca con los microservicios directamente.

auth-service:

Registro

Login

Generación de JWT

Validación

Tablas:

usuarios

catalog-service:

Listado de productos

Filtros / búsqueda

Detalle de productos

Tablas:

productos

categoria

cart-service:

Crear y recuperar carrito

Agregar / remover ítems

Checkout

Tablas:

carrito

carrito_items

5) Integración App ↔ Backend
La app usa una única URL:
private const val BASE_URL = "http://192.168.X.X:8080/"


El gateway enruta automáticamente hacia el microservicio correcto.

</Token JWT

Se envía por defecto en llamadas protegidas:

Authorization: Bearer <TOKEN>

</ OpenWeather (NO pasa por el gateway)

La app se conecta directamente:

https://api.openweathermap.org/data/2.5/weather

6) Estructura del Proyecto Android
app/
├─ data/
│  ├─ remote/ (APIs + DTOs + Retrofit)
│  ├─ local/ (Room)
│  ├─ datastore/ (DataStore)
├─ ui/
│  ├─ screens/ (auth, home, catalog, detail, cart, device…)
│  ├─ components/
│  ├─ session/
│  └─ theme/
├─ navigation/
└─ MainActivity.kt

7) Pruebas Unitarias

Se implementaron tests para ViewModels independientes del framework Android:

Test	Objetivo
CartViewModelTest	Manejo del carrito sin token (error esperado)
ProductDetailViewModelTest	Validación de token nulo
WeatherViewModelTest	Estado inicial correcto

Todos se ejecutan con JUnit y pasan correctamente.

8) Ejecución del Proyecto
A) Microservicios + Gateway

Levantar MySQL

Ejecutar individualmente:

auth-service

catalog-service

cart-service

gateway-service

Gateway quedará disponible en:
-> http://<tu-ip-local>:8080

B) Aplicación Android

Abrir en Android Studio

Reemplazar IP en ApiClient.kt

Ejecutar en emulador o dispositivo físico

La app se conectará automáticamente al Gateway

C) APK Firmada

Generada mediante:

Build → Generate Signed APK → release

APK lista para instalar en equipos Android reales.

9) Permisos (AndroidManifest)
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
<uses-permission android:name="android.permission.CAMERA"/>

<application android:usesCleartextTraffic="true"/>

10) Dependencias Principales

Jetpack Compose BOM

Material 3

Navigation Compose

Retrofit + Gson

DataStore

Room

Coil

Play Services Location

Kotlin Coroutines

11) Conclusiones

Arquitectura profesional basada en microservicios escalables

API Gateway centraliza toda comunicación interna

App Android moderna con Compose + MVVM

Integración con sensores (cámara + GPS)

Consumo directo de API externa

Pruebas unitarias implementadas

APK firmada lista para distribución

Este proyecto demuestra dominio completo de:

Android moderno

Spring Boot

Microservicios

Networking avanzado

Persistencia local

Arquitectura limpia

12) Mejoras Futuras

Pasarela de pagos

Módulo de pedidos

Carrito completamente offline

Notificaciones push

Temas dinámicos / animaciones

Panel web de administración

13) Nota del Autor

HuertoHogar integra tecnologías actuales y patrones de arquitectura profesional, demostrando una solución móvil + backend bien estructurada, escalable y mantenible.

DATO IMPORTANTE:
UN BUEN % DE ESTE README FUE CREADO POR COPILOT Y EL OTRO % FUE ESCRITO A MANO
