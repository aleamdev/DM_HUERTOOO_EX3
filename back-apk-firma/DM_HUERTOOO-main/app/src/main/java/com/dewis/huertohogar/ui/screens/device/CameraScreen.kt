package com.dewis.huertohogar.ui.screens.device

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraScreen() {
    var photo by remember { mutableStateOf<Bitmap?>(null) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { photo = it }

    Scaffold(topBar = { TopAppBar(title = { Text("Cámara (Preview)") }) }) { pad ->
        Column(Modifier.padding(pad).fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(Modifier.height(16.dp))
            Button(onClick = { launcher.launch(null) }) { Text("Tomar foto rápida") }
            Spacer(Modifier.height(16.dp))
            if (photo != null) Image(photo!!.asImageBitmap(), null, Modifier.size(220.dp)) else Text("Sin foto aún")
        }
    }
}