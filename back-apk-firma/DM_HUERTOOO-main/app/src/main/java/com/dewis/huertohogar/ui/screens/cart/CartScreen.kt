package com.dewis.huertohogar.ui.screens.cart

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.dewis.huertohogar.ui.session.SessionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    onBack: () -> Unit,
    vm: CartViewModel = viewModel()
) {
    val ui by vm.ui.collectAsState()
    val sessionVm: SessionViewModel = viewModel()
    val token by sessionVm.token.collectAsState(initial = null)

    LaunchedEffect(token) {
        if (!token.isNullOrBlank()) {
            vm.loadCart(token)
        }
    }

    if (ui.success != null) {
        AlertDialog(
            onDismissRequest = { vm.clearMessages() },
            confirmButton = {
                TextButton(onClick = { vm.clearMessages() }) {
                    Text("Aceptar")
                }
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
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { pad ->
        Column(
            Modifier
                .padding(pad)
                .fillMaxSize()
        ) {
            when {
                ui.loading -> {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                ui.error != null -> {
                    Column(Modifier.padding(16.dp)) {
                        Text(ui.error ?: "", color = MaterialTheme.colorScheme.error)
                    }
                }

                else -> {
                    val cart = ui.cart
                    if (cart == null || cart.items.isEmpty()) {
                        Column(
                            Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        ) {
                            Text("Tu carrito está vacío.")
                        }
                    } else {
                        LazyColumn(
                            Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(cart.items) { item ->
                                Card {
                                    Row(
                                        Modifier
                                            .fillMaxWidth()
                                            .padding(12.dp)
                                    ) {
                                        AsyncImage(
                                            model = item.imagenUrl,
                                            contentDescription = null,
                                            modifier = Modifier
                                                .size(64.dp)
                                                .padding(end = 8.dp)
                                        )
                                        Column(Modifier.weight(1f)) {
                                            Text(
                                                item.nombre,
                                                style = MaterialTheme.typography.titleSmall
                                            )
                                            Text("Código: ${item.codigo}")
                                            Text("Cantidad: ${item.cantidad}")
                                            Text("Subtotal: ${item.subtotal} CLP")
                                        }
                                    }
                                }
                            }
                        }

                        Divider()
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                "Total: ${cart.total} CLP",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(Modifier.height(8.dp))
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
