package com.dewis.dm_huertohogar_ex3.ui.screens.device

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dewis.dm_huertohogar_ex3.data.remote.ApiClient
import com.dewis.dm_huertohogar_ex3.ui.screens.device.WeatherViewModel
import com.google.android.gms.location.LocationServices

private const val OW_KEY = "90233d19879688992cb531a79bb705ed"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationScreen() {

    val ctx = LocalContext.current
    var lastLocation by remember { mutableStateOf<Location?>(null) }
    var granted by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                ctx,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val weatherVm: WeatherViewModel = viewModel()
    val weatherUi by weatherVm.ui.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Ubicación y clima") }) }
    ) { pad ->

        Column(
            modifier = Modifier
                .padding(pad)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.Start
        ) {

            // ---------------------------
            // PERMISOS
            // ---------------------------

            Text(
                "Estado del permiso de ubicación",
                style = MaterialTheme.typography.titleMedium
            )

            Surface(
                tonalElevation = 2.dp,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Column {
                        Text(
                            if (granted) "Permiso concedido" else "Permiso no concedido",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            if (granted)
                                "Puedes obtener la última ubicación conocida."
                            else
                                "Debes otorgar el permiso desde Ajustes.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    AssistChip(
                        onClick = {
                            granted = ContextCompat.checkSelfPermission(
                                ctx,
                                Manifest.permission.ACCESS_FINE_LOCATION
                            ) == PackageManager.PERMISSION_GRANTED
                        },
                        label = { Text("Refrescar") }
                    )
                }
            }

            Divider()

            // ---------------------------
            // OBTENER UBICACIÓN
            // ---------------------------

            Text(
                "Última ubicación",
                style = MaterialTheme.typography.titleMedium
            )

            Surface(
                tonalElevation = 1.dp,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    Modifier
                        .padding(12.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    Text(
                        text = if (lastLocation != null)
                            "Lat: ${lastLocation!!.latitude}\nLng: ${lastLocation!!.longitude}"
                        else
                            "No disponible",
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Button(
                        onClick = {
                            if (!granted) return@Button
                            LocationServices.getFusedLocationProviderClient(ctx)
                                .lastLocation
                                .addOnSuccessListener { loc ->
                                    lastLocation = loc

                                    if (loc != null) {
                                        weatherVm.loadWeather(
                                            lat = loc.latitude,
                                            lon = loc.longitude,
                                            key = OW_KEY
                                        )
                                    }
                                }
                                .addOnFailureListener { e ->
                                    lastLocation = null
                                }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Obtener ubicación")
                    }
                }
            }

            Divider()

            // ---------------------------
            // CLIMA
            // ---------------------------

            Text(
                "Clima actual",
                style = MaterialTheme.typography.titleMedium
            )

            when {
                weatherUi.loading -> {
                    CircularProgressIndicator()
                }

                weatherUi.error != null -> {
                    Text(
                        text = weatherUi.error!!,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                weatherUi.data != null -> {
                    val w = weatherUi.data!!

                    Surface(
                        tonalElevation = 2.dp,
                        shape = MaterialTheme.shapes.medium,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(Modifier.padding(16.dp)) {

                            Text(
                                "Ubicación: ${w.name}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(Modifier.height(8.dp))

                            Text("Temperatura: ${w.main.temp}°C")
                            Text("Humedad: ${w.main.humidity}%")
                            Text("Clima: ${w.weather.firstOrNull()?.description ?: "—"}")
                        }
                    }
                }
            }
        }
    }
}
