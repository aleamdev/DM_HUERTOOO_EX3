package com.dewis.dm_huertohogar_ex3.ui.screens.catalog

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.dewis.dm_huertohogar_ex3.data.local.Product
import com.dewis.dm_huertohogar_ex3.ui.session.SessionViewModel
import com.dewis.dm_huertohogar_ex3.ui.screens.cart.CartViewModel


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

    val query = q?.trim()?.lowercase() ?: ""
    val filtered = remember(all, query, cat) {
        all.filter { p ->
            val matchesQuery = query.isBlank() ||
                    p.name.lowercase().contains(query) ||
                    p.sku.lowercase().contains(query) ||
                    (p.description?.lowercase()?.contains(query) == true)

            val matchesCategory =
                cat.isNullOrBlank() || p.categoryMatches(cat)

            matchesQuery && matchesCategory
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Catálogo") },
                actions = {
                    TextButton(onClick = goToCamera) { Text("Cámara") }
                    TextButton(onClick = goToLocation) { Text("Ubicación") }
                }
            )
        }
    ) { pad ->
        Column(
            modifier = Modifier
                .padding(pad)
                .fillMaxSize()
        ) {

            if (error != null) {
                ErrorBanner(error!!)
            }

            if (cartUi.error != null) {
                ErrorBanner(cartUi.error!!)
            }

            if (cartUi.success != null) {
                SuccessBanner(cartUi.success!!)
            }

            if (filtered.isEmpty()) {
                EmptyResults()
                return@Column
            }

            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 170.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filtered) { p ->
                    ProductCard(
                        product = p,
                        onAdd = { cartVm.addItem(token, p.id.toLong(), 1) },
                        onClick = { goToDetail(p.id) }
                    )
                }
            }
        }
    }
}

private fun Product.categoryMatches(cat: String): Boolean {
    return when (cat.trim()) {
        "Frutas frescas"      -> sku.startsWith("FR", true)
        "Verduras orgánicas"  -> sku.startsWith("VR", true)
        "Productos orgánicos" -> sku.startsWith("PO", true)
        "Productos lácteos"   -> sku.startsWith("PL", true)
        else -> true
    }
}


@Composable
private fun ErrorBanner(text: String) {
    Surface(
        color = MaterialTheme.colorScheme.errorContainer,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text,
            color = MaterialTheme.colorScheme.onErrorContainer,
            modifier = Modifier.padding(12.dp)
        )
    }
}

@Composable
private fun SuccessBanner(text: String) {
    Surface(
        color = MaterialTheme.colorScheme.primaryContainer,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.padding(12.dp)
        )
    }
}

@Composable
private fun EmptyResults() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("No se encontraron productos", fontSize = 18.sp)
        Text("Prueba otra búsqueda", color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}



@Composable
fun ProductCard(
    product: Product,
    onAdd: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Column(Modifier.padding(12.dp)) {

            AsyncImage(
                model = product.imageUrl,
                contentDescription = product.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(140.dp)
                    .fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            Text(
                product.name,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                maxLines = 2
            )

            Text(
                product.sku,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = product.priceText,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = onAdd,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Agregar al carrito")
            }
        }
    }
}
