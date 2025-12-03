package com.dewis.huertohogar.ui.screens.cart

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dewis.huertohogar.data.remote.ApiClient
import com.dewis.huertohogar.data.remote.CarritoItemRequestDto
import com.dewis.huertohogar.data.remote.CarritoResponseDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class CartUiState(
    val loading: Boolean = false,
    val error: String? = null,
    val success: String? = null,
    val cart: CarritoResponseDto? = null
)

class CartViewModel(app: Application) : AndroidViewModel(app) {

    private val _ui = MutableStateFlow(CartUiState())
    val ui: StateFlow<CartUiState> = _ui

    private fun auth(token: String) = "Bearer $token"

    fun clearMessages() {
        _ui.value = _ui.value.copy(error = null, success = null)
    }

    fun loadCart(token: String?) {
        if (token.isNullOrBlank()) {
            _ui.value = _ui.value.copy(error = "Debes iniciar sesión para ver el carrito.")
            return
        }

        viewModelScope.launch {
            try {
                _ui.value = _ui.value.copy(loading = true, error = null, success = null)
                val cart = ApiClient.cartApi.verCarrito(auth(token))
                _ui.value = _ui.value.copy(loading = false, cart = cart)
            } catch (e: Exception) {
                _ui.value = _ui.value.copy(
                    loading = false,
                    error = "No se pudo cargar el carrito."
                )
            }
        }
    }

    fun addItem(token: String?, productoId: Long, cantidad: Int = 1) {
        if (token.isNullOrBlank()) {
            _ui.value = _ui.value.copy(error = "Debes iniciar sesión para agregar productos.")
            return
        }

        viewModelScope.launch {
            try {
                _ui.value = _ui.value.copy(loading = true, error = null, success = null)
                val body = CarritoItemRequestDto(productoId = productoId, cantidad = cantidad)
                val cart = ApiClient.cartApi.agregarItem(auth(token), body)
                _ui.value = _ui.value.copy(
                    loading = false,
                    cart = cart,
                    success = "Producto agregado al carrito."
                )
            } catch (e: Exception) {
                _ui.value = _ui.value.copy(
                    loading = false,
                    error = "No se pudo agregar el producto al carrito."
                )
            }
        }
    }

    fun checkout(token: String?) {
        if (token.isNullOrBlank()) {
            _ui.value = _ui.value.copy(error = "Debes iniciar sesión para comprar.")
            return
        }

        viewModelScope.launch {
            try {
                _ui.value = _ui.value.copy(loading = true, error = null, success = null)
                ApiClient.cartApi.checkout(auth(token))
                val cartVacio = ApiClient.cartApi.verCarrito(auth(token))
                _ui.value = _ui.value.copy(
                    loading = false,
                    cart = cartVacio,
                    success = "Compra realizada con éxito"
                )
            } catch (e: Exception) {
                _ui.value = _ui.value.copy(
                    loading = false,
                    error = "No se pudo completar la compra."
                )
            }
        }
    }
}
