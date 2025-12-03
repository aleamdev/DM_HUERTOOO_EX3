package com.dewis.huertohogar.ui.screens.device

import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationScreen() {
    val ctx = LocalContext.current
    var last by remember { mutableStateOf("—") }
    var granted by remember {
        mutableStateOf(ContextCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
    }

    Scaffold(topBar = { TopAppBar(title = { Text("Ubicación") }) }) { pad ->
        Column(Modifier.padding(pad).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Permiso FINE: ${if (granted) "OK" else "No"}")
            Button(onClick = {
                granted = ContextCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                if (!granted) return@Button
                LocationServices.getFusedLocationProviderClient(ctx).lastLocation
                    .addOnSuccessListener { loc -> last = if (loc != null) "${loc.latitude}, ${loc.longitude}" else "No disponible" }
                    .addOnFailureListener { last = "Error: ${it.message}" }
            }) { Text("Obtener ubicación") }
            Text("Última: $last")
        }
    }
}