package com.dewis.huertohogar.ui.screens.detail

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.dewis.huertohogar.data.remote.ApiClient
import com.dewis.huertohogar.data.remote.ProductoResponseDto
import com.dewis.huertohogar.ui.session.SessionViewModel
import kotlinx.coroutines.launch

data class DetailUiState(
    val loading: Boolean = false,
    val error: String? = null,
    val product: ProductoResponseDto? = null
)

class ProductDetailViewModel : ViewModel() {

    var ui by mutableStateOf(DetailUiState())
        private set

    fun load(token: String?, id: Long) {
        if (token.isNullOrBlank()) {
            ui = ui.copy(error = "Debes iniciar sesión para ver el detalle.")
            return
        }
        viewModelScope.launch {
            try {
                ui = ui.copy(loading = true, error = null)
                val auth = "Bearer $token"
                val p = ApiClient.productApi.obtenerProducto(auth, id)
                ui = ui.copy(loading = false, product = p)
            } catch (e: Exception) {
                ui = ui.copy(loading = false, error = "No se pudo cargar el producto.")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(productId: String) {
    val vm: ProductDetailViewModel = viewModel()
    val sessionVm: SessionViewModel = viewModel()
    val token by sessionVm.token.collectAsState(initial = null)

    LaunchedEffect(token, productId) {
        val id = productId.toLongOrNull()
        if (id != null) {
            vm.load(token, id)
        }
    }

    val ui = vm.ui

    Scaffold(
        topBar = { TopAppBar(title = { Text("Detalle del producto") }) }
    ) { pad ->
        Column(
            Modifier
                .padding(pad)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            when {
                ui.loading -> {
                    CircularProgressIndicator()
                }

                ui.error != null -> {
                    Text(ui.error ?: "", color = MaterialTheme.colorScheme.error)
                }

                ui.product != null -> {
                    val p = ui.product
                    Text(p!!.nombre, style = MaterialTheme.typography.headlineSmall)
                    Spacer(Modifier.height(8.dp))
                    AsyncImage(
                        model = p.imagenUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    )
                    Spacer(Modifier.height(8.dp))
                    Text("Código: ${p.codigo}")
                    Text("Precio: ${p.precio} CLP")
                    Text("Stock: ${p.stock} ${p.unidad ?: ""}")
                    Spacer(Modifier.height(8.dp))
                    Text("Categoría: ${p.categoriaNombre}")
                    Spacer(Modifier.height(8.dp))
                    Text(p.descripcion ?: "Sin descripción")
                }

                else -> {
                    Text("Producto no encontrado.")
                }
            }
        }
    }
}
