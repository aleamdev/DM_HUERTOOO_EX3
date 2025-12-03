package com.dewis.dm_huertohogar_ex3.ui.screens.cart

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.dewis.dm_huertohogar_ex3.data.remote.CarritoItemResponseDto
import com.dewis.dm_huertohogar_ex3.ui.session.SessionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    onBack: () -> Unit,
    vm: CartViewModel = viewModel()
) {
    val ui by vm.ui.collectAsState()
    val sessionVm: SessionViewModel = viewModel()
    val token by sessionVm.token.collectAsState(initial = null)

    // Cargar carrito al tener token
    LaunchedEffect(token) {
        if (!token.isNullOrBlank()) vm.loadCart(token)
    }

    // Diálogo de éxito
    if (ui.success != null) {
        AlertDialog(
            onDismissRequest = { vm.clearMessages() },
            confirmButton = {
                TextButton(onClick = { vm.clearMessages() }) { Text("Aceptar") }
            },
            title = { Text("Compra realizada") },
            text = { Text(ui.success ?: "Compra realizada con éxito") }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi carrito") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { pad ->

        Column(
            modifier = Modifier
                .padding(pad)
                .fillMaxSize()
        ) {
            when {
                ui.loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                ui.error != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            ui.error ?: "",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }

                else -> {
                    val cart = ui.cart

                    if (cart == null || cart.items.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Tu carrito está vacío.")
                        }
                    } else {

                        LazyColumn(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(cart.items) { item ->
                                CartItemCard(
                                    item = item,
                                    onAdd = { vm.addItem(token, item.productoId, 1) },
                                    onRemove = { vm.removeItem(token, item.productoId) }
                                )
                            }
                        }

                        Divider()

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                "Total: ${cart.total} CLP",
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.Bold
                                )
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Button(
                                onClick = { vm.checkout(token) },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Proceder compra")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CartItemCard(
    item: CarritoItemResponseDto,
    onAdd: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            AsyncImage(
                model = item.imagenUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(70.dp)
                    .padding(end = 10.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(item.nombre, fontWeight = FontWeight.Bold)
                Text("Código: ${item.codigo}")
                Text("Subtotal: ${item.subtotal} CLP")
                Text("Cantidad: ${item.cantidad}")
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Button(
                    onClick = onAdd,
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                ) { Text("+") }

                Button(
                    onClick = onRemove,
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                ) { Text("-") }
            }
        }
    }
}
