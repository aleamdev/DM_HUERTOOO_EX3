@file:OptIn(ExperimentalMaterial3Api::class)
package com.dewis.dm_huertohogar_ex3.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AppTopBar(
    email: String?,
    onSearch: (String) -> Unit,
    onSelect: (String) -> Unit
) {
    var q by remember { mutableStateOf("") }
    var catOpen by remember { mutableStateOf(false) }
    var accountOpen by remember { mutableStateOf(false) }

    TopAppBar(
        title = { Text("HuertoHogar") },
        actions = {

            IconButton(onClick = { accountOpen = true }) {
                Icon(Icons.Default.Person, contentDescription = "Cuenta")
            }
            DropdownMenu(expanded = accountOpen, onDismissRequest = { accountOpen = false }) {
                if (email.isNullOrBlank()) {
                    DropdownMenuItem(
                        text = { Text("Ingresar cuenta") },
                        onClick = { accountOpen = false; onSelect("login") }
                    )
                } else {
                    DropdownMenuItem(
                        text = { Text(if (email.equals("invitado@huertohogar.cl", true)) "Invitado" else email) },
                        onClick = { }
                    )
                    Divider()
                    DropdownMenuItem(
                        text = { Text("Cerrar sesión") },
                        onClick = { accountOpen = false; onSelect("logout") }
                    )
                }
                Divider()
                DropdownMenuItem(text = { Text("Inicio") }, onClick = { accountOpen = false; onSelect("home") })
                DropdownMenuItem(text = { Text("Catálogo") }, onClick = { accountOpen = false; onSelect("catalog") })
                DropdownMenuItem(text = { Text("Cámara") }, onClick = { accountOpen = false; onSelect("camera") })
                DropdownMenuItem(text = { Text("Ubicación") }, onClick = { accountOpen = false; onSelect("location") })
                DropdownMenuItem(text = { Text("Carrito") }, onClick = { accountOpen = false; onSelect("cart") })
            }

            Box {
                TextButton(onClick = { catOpen = true }) { Text("Categorías") }
                DropdownMenu(expanded = catOpen, onDismissRequest = { catOpen = false }) {
                    listOf("Frutas frescas","Verduras orgánicas","Productos orgánicos","Productos lácteos").forEach {
                        DropdownMenuItem(
                            text = { Text(it) },
                            onClick = { catOpen = false; onSelect("catalog:$it") }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = q,
                onValueChange = { q = it },
                placeholder = { Text("Buscar productos") },
                singleLine = true,
                modifier = Modifier.width(160.dp)
            )
            IconButton(onClick = { onSearch(q) }) {
                Icon(Icons.Default.Search, contentDescription = "Buscar")
            }
        }
    )
}
