package com.dewis.huertohogar.ui.screens.catalog

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dewis.huertohogar.data.local.AppDatabase
import com.dewis.huertohogar.data.local.Product
import com.dewis.huertohogar.data.remote.ApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CatalogViewModel(app: Application) : AndroidViewModel(app) {

    private val dao = AppDatabase.get(app).productDao()

    val products: StateFlow<List<Product>> =
        dao.observeAll().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadFromBackend(token: String?) = viewModelScope.launch {
        if (token.isNullOrBlank()) {
            _error.value = "Inicia sesión para ver productos reales del backend."
            return@launch
        }

        try {
            _error.value = null
            val auth = "Bearer $token"
            val remote = ApiClient.productApi.listarProductos(auth)

            dao.clear()

            remote.forEach { dto ->
                val p = Product(
                    id = dto.id.toInt(),              // usamos el ID del backend
                    sku = dto.codigo,
                    name = dto.nombre,
                    imageUrl = dto.imagenUrl ?: "",
                    priceText = "${dto.precio} CLP",
                    stockText = "Stock: ${dto.stock} ${dto.unidad ?: ""}",
                    description = dto.descripcion ?: "",
                    isFeatured = false,
                    oldPriceText = null
                )
                dao.upsert(p)
            }

        } catch (e: Exception) {
            _error.value = "No se pudieron cargar productos desde el backend."
        }
    }

    fun clear() = viewModelScope.launch { dao.clear() }
}
