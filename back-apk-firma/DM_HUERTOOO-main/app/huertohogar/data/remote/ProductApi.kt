package com.dewis.huertohogar.data.remote

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

// Coincide con ProductoResponse del backend
data class ProductoResponseDto(
    val id: Long,
    val codigo: String,
    val nombre: String,
    val precio: Int,
    val stock: Int,
    val unidad: String?,
    val descripcion: String?,
    val imagenUrl: String?,
    val categoriaId: Long,
    val categoriaNombre: String
)

interface ProductApi {

    @GET("productos")
    suspend fun listarProductos(
        @Header("Authorization") auth: String,
        @Query("nombre") nombre: String? = null,
        @Query("categoriaId") categoriaId: Long? = null
    ): List<ProductoResponseDto>

    @GET("productos/{id}")
    suspend fun obtenerProducto(
        @Header("Authorization") auth: String,
        @Path("id") id: Long
    ): ProductoResponseDto
}
