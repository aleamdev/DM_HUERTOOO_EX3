package com.dewis.dm_huertohogar_ex3.data.remote

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

data class CarritoItemRequestDto(
    val productoId: Long,
    val cantidad: Int
)

data class CarritoItemResponseDto(
    val productoId: Long,
    val codigo: String,
    val nombre: String,
    val imagenUrl: String?,
    val precioUnitario: Int,
    val cantidad: Int,
    val subtotal: Int
)

data class CarritoResponseDto(
    val id: Long?,
    val estado: String,
    val items: List<CarritoItemResponseDto> = emptyList(),
    val total: Int = 0
)

interface CartApi {

    @GET("carrito")
    suspend fun verCarrito(
        @Header("Authorization") auth: String
    ): CarritoResponseDto

    @POST("carrito/items")
    suspend fun agregarItem(
        @Header("Authorization") auth: String,
        @Body body: CarritoItemRequestDto
    ): CarritoResponseDto

    @DELETE("carrito/items/{productoId}")
    suspend fun eliminarItem(
        @Header("Authorization") auth: String,
        @Path("productoId") productoId: Long
    ): CarritoResponseDto

    @POST("carrito/checkout")
    suspend fun checkout(
        @Header("Authorization") auth: String
    ): CarritoResponseDto

    @DELETE("carrito")
    suspend fun vaciar(
        @Header("Authorization") auth: String
    ): CarritoResponseDto
}
