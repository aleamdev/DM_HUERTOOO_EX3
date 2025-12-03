package com.dewis.huertohogar.ui.screens.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun LoginScreen(
    onLoggedIn: () -> Unit,
    goToRegister: () -> Unit, // no lo usamos, pero lo dejamos
    vm: AuthViewModel = viewModel()
) {
    val s by vm.ui.collectAsState()
    var showRegister by remember { mutableStateOf(false) }

    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Card(Modifier.padding(16.dp)) {
            Column(
                Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    if (showRegister) "Crear cuenta" else "Ingreso",
                    style = MaterialTheme.typography.titleLarge
                )

                if (showRegister) {
                    OutlinedTextField(
                        value = s.name,
                        onValueChange = vm::onName,
                        label = { Text("Nombre") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                        isError = s.nameError != null,
                        modifier = Modifier.fillMaxWidth()
                    )
                    AnimatedVisibility(
                        visible = s.nameError != null,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Text(
                            s.nameError ?: "",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                OutlinedTextField(
                    value = s.email,
                    onValueChange = vm::onEmail,
                    label = { Text("Email") },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                    isError = s.emailError != null,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth()
                )
                AnimatedVisibility(
                    visible = s.emailError != null,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Text(
                        s.emailError ?: "",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                OutlinedTextField(
                    value = s.password,
                    onValueChange = vm::onPass,
                    label = { Text("Contraseña") },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                    isError = s.passwordError != null,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )
                AnimatedVisibility(
                    visible = s.passwordError != null,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Text(
                        s.passwordError ?: "",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                if (s.generalError != null) {
                    Text(
                        s.generalError!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(Modifier.height(8.dp))
                Divider()

                OutlinedButton(
                    onClick = { vm.guest(onLoggedIn) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Entrar como invitado")
                }

                if (showRegister) {
                    Button(
                        onClick = { vm.register { onLoggedIn() } },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !s.isLoading
                    ) {
                        Text(if (s.isLoading) "Creando cuenta..." else "Registrarme")
                    }
                    TextButton(
                        onClick = { showRegister = false },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Ya tengo cuenta")
                    }
                } else {
                    Button(
                        onClick = { vm.login(onLoggedIn) },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !s.isLoading
                    ) {
                        Text(if (s.isLoading) "Ingresando..." else "Ingresar")
                    }
                    TextButton(
                        onClick = { showRegister = true },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Crear cuenta")
                    }
                }
            }
        }
    }
}
