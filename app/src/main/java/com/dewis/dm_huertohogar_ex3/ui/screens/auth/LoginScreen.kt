package com.dewis.dm_huertohogar_ex3.ui.screens.auth

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoggedIn: () -> Unit,
    goToRegister: () -> Unit,
    vm: AuthViewModel = viewModel()
) {
    val s by vm.ui.collectAsState()
    var isRegister by remember { mutableStateOf(false) }

    // Obtener tamaño de pantalla para responsive
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val cardWidth = if (screenHeight < 600.dp) 340.dp else 420.dp

    Scaffold { pad ->
        Box(
            modifier = Modifier
                .padding(pad)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            Card(
                modifier = Modifier
                    .width(cardWidth)
                    .padding(16.dp),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    // TÍTULO
                    Text(
                        if (isRegister) "Crear cuenta" else "Iniciar sesión",
                        style = MaterialTheme.typography.headlineSmall
                    )

                    // CAMPO NOMBRE (solo en registro)
                    AnimatedVisibility(
                        visible = isRegister,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        Column(Modifier.fillMaxWidth()) {
                            OutlinedTextField(
                                value = s.name,
                                onValueChange = vm::onName,
                                label = { Text("Nombre completo") },
                                leadingIcon = { Icon(Icons.Default.Person, null) },
                                isError = s.nameError != null,
                                modifier = Modifier.fillMaxWidth()
                            )
                            if (s.nameError != null) {
                                Text(
                                    s.nameError!!,
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }

                    // CAMPO EMAIL
                    Column(Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = s.email,
                            onValueChange = vm::onEmail,
                            label = { Text("Correo electrónico") },
                            leadingIcon = { Icon(Icons.Default.Email, null) },
                            isError = s.emailError != null,
                            modifier = Modifier.fillMaxWidth()
                        )
                        if (s.emailError != null) {
                            Text(
                                s.emailError!!,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }

                    // CAMPO PASSWORD
                    Column(Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = s.password,
                            onValueChange = vm::onPass,
                            label = { Text("Contraseña") },
                            leadingIcon = { Icon(Icons.Default.Lock, null) },
                            visualTransformation = PasswordVisualTransformation(),
                            isError = s.passwordError != null,
                            modifier = Modifier.fillMaxWidth()
                        )
                        if (s.passwordError != null) {
                            Text(
                                s.passwordError!!,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }

                    // ERROR GENERAL
                    if (s.generalError != null) {
                        Text(
                            s.generalError!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }

                    Spacer(Modifier.height(4.dp))

                    // BOTÓN PRINCIPAL
                    Button(
                        onClick = {
                            if (isRegister) vm.register { onLoggedIn() }
                            else vm.login(onLoggedIn)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !s.isLoading
                    ) {
                        if (s.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        } else {
                            Text(if (isRegister) "Registrarme" else "Ingresar")
                        }
                    }

                    // CAMBIAR ENTRE LOGIN / REGISTER
                    TextButton(onClick = { isRegister = !isRegister }) {
                        Text(
                            if (isRegister) "Ya tengo una cuenta" else "Crear nueva cuenta"
                        )
                    }

                    Divider()

                    // INVITADO
                    OutlinedButton(
                        onClick = { vm.guest(onLoggedIn) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Person, null)
                        Spacer(Modifier.width(8.dp))
                        Text("Entrar como invitado")
                    }
                }
            }
        }
    }
}
