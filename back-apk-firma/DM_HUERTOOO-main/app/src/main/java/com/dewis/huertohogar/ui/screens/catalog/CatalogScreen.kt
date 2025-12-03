package com.dewis.huertohogar.ui.screens.catalog

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.dewis.huertohogar.data.local.Product
import com.dewis.huertohogar.ui.session.SessionViewModel
import com.dewis.huertohogar.ui.screens.cart.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
    q: String?,
    cat: String?,
    goToDetail: (Int) -> Unit,
    goToCamera: () -> Unit,
    goToLocation: () -> Unit,
    vm: CatalogViewModel = viewModel()
) {
    val all by vm.products.collectAsState()
    val error by vm.error.collectAsState()

    val sessionVm: SessionViewModel = viewModel()
    val token by sessionVm.token.collectAsState(initial = null)

    val cartVm: CartViewModel = viewModel()
    val cartUi by cartVm.ui.collectAsState()

    LaunchedEffect(token) {
        vm.clear()
        vm.loadFromBackend(token)
    }

    fun matchesCategory(p: Product, c: String?): Boolean = when (c?.trim()) {
        "Frutas frescas"      -> p.sku.startsWith("FR", ignoreCase = true)
        "Verduras orgánicas"  -> p.sku.startsWith("VR", ignoreCase = true)
        "Productos orgánicos" -> p.sku.startsWith("PO", ignoreCase = true)
        "Productos lácteos"   -> p.sku.startsWith("PL", ignoreCase = true)
        null, ""              -> true
        else                  -> true
    }

    val query = (q ?: "").trim().lowercase()
    val filtered = remember(all, query, cat) {
        all.filter { p ->
            val okQ = query.isBlank() ||
                    p.name.lowercase().contains(query) ||
                    p.sku.lowercase().contains(query) ||
                    (p.description?.lowercase()?.contains(query) == true) ||
                    (p.priceText.lowercase().contains(query))
            val okC = matchesCategory(p, cat)
            okQ && okC
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    val chips = listOfNotNull(
                        cat?.takeIf { it.isNotBlank() }?.let { "Categoría: $it" },
                        query.takeIf { it.isNotBlank() }?.let { "Búsqueda: $it" }
                    )
                    Text(if (chips.isEmpty()) "Catálogo" else chips.joinToString("  •  "))
                },
                actions = {
                    TextButton(onClick = goToCamera) { Text("Cámara") }
                    TextButton(onClick = goToLocation) { Text("Ubicación") }
                }
            )
        }
    ) { pad ->

        Column(
            Modifier
                .padding(pad)
                .fillMaxSize()
        ) {
            if (error != null) {
                Text(
                    error ?: "",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(8.dp)
                )
            }

            if (cartUi.error != null) {
                Text(
                    cartUi.error ?: "",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(8.dp)
                )
            }
            if (cartUi.success != null) {
                Text(
                    cartUi.success ?: "",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(8.dp)
                )
            }

            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 240.dp),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filtered) { p ->
                    ProductCard(
                        p,
                        onAdd = {
                            cartVm.addItem(token, p.id.toLong(), 1)
                        }
                    ) { goToDetail(p.id) }
                }
            }
        }
    }
}

@Composable
private fun ProductCard(p: Product, onAdd: () -> Unit, onClick: () -> Unit) {
    Card(onClick = onClick) {
        Column(Modifier.padding(12.dp)) {
            AsyncImage(
                model = p.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            )
            Spacer(Modifier.height(8.dp))
            Text("${p.sku} - ${p.name}", style = MaterialTheme.typography.titleSmall)
            if (p.priceText.isNotBlank()) {
                Text("Precio: ${p.priceText}", style = MaterialTheme.typography.bodyMedium)
            }
            p.description?.let {
                Text(it, style = MaterialTheme.typography.bodySmall, maxLines = 3)
            }
            Spacer(Modifier.height(8.dp))
            Button(onClick = onAdd) { Text("Agregar al carrito") }
        }
    }
}
