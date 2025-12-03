package com.dewis.dm_huertohogar_ex3.ui.screens.home

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dewis.dm_huertohogar_ex3.ui.components.AppTopBar
import com.dewis.dm_huertohogar_ex3.ui.session.SessionViewModel
import com.dewis.dm_huertohogar_ex3.ui.screens.catalog.CatalogViewModel


@Composable
fun HomeScreen(
    onSearch: (String) -> Unit,
    onSelect: (String) -> Unit
) {
    val ctx = LocalContext.current
    val sessionVm: SessionViewModel = viewModel()
    val email by sessionVm.email.collectAsState(initial = null)

    val catalogVm: CatalogViewModel = viewModel()
    val destacados = catalogVm.getDestacados()

    fun handleSelect(key: String) {
        if (key == "logout") {
            sessionVm.logout { onSelect("logout") }
        } else {
            onSelect(key)
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                email = email,
                onSearch = { q -> onSearch(q.trim()) },
                onSelect = ::handleSelect
            )
        }
    ) { pad ->

        val screenWidth = LocalConfiguration.current.screenWidthDp.dp

        Column(
            Modifier
                .padding(pad)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

            HeroSection(
                title = "Frescura Directa del Campo",
                subtitle = "Descubre productos orgánicos frescos y locales",
                imageUrl = "https://images.unsplash.com/photo-1461354464878-ad92f492a5a0",
                ctaText = "Ver catálogo",
                onClickCta = { onSelect("catalog") },
                screenWidth = screenWidth
            )

            CategoryChips(
                listOf("Frutas frescas", "Verduras orgánicas", "Productos orgánicos", "Productos lácteos")
            ) { cat ->
                onSelect("catalog:$cat")
            }

            SectionTitle("Productos Destacados")

            DestacadosRow(
                productos = destacados,
                onOpen = { p -> onSelect("detail/${p.id}") },
                onAdd = { p -> onSelect("catalog") }
            )

            InfoBanner(
                title = "Producción local y sostenible",
                bullets = listOf(
                    "Cultivos libres de pesticidas",
                    "Apoyamos a agricultores de la zona",
                    "Entrega rápida en todo Santiago"
                ),
                screenWidth = screenWidth
            )

            SectionTitle("Nuestras Ubicaciones")

            LocationsRow(
                cities = listOf("Santiago", "Valparaíso", "Rancagua"),
                openMaps = { city ->
                    val url = "https://maps.google.com/?q=${Uri.encode("HuertoHogar $city")}"
                    ctx.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                }
            )

            Spacer(Modifier.height(24.dp))
        }
    }
}


// -------------------------------------------------------------
// COMPONENTES
// -------------------------------------------------------------


@Composable
private fun HeroSection(
    title: String,
    subtitle: String,
    imageUrl: String,
    ctaText: String,
    onClickCta: () -> Unit,
    screenWidth: Dp
) {
    Box(
        Modifier
            .fillMaxWidth()
            .height(220.dp)
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )
        Box(
            Modifier
                .matchParentSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xAA000000), Color.Transparent, Color(0xAA000000))
                    )
                )
        )
        Column(
            Modifier
                .matchParentSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(1.dp))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(title, style = MaterialTheme.typography.titleLarge, color = Color.White)
                Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = Color.White)
            }
            Button(onClick = onClickCta) { Text(ctaText) }
        }
    }
}



@Composable
private fun SectionTitle(text: String) {
    Text(
        text,
        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
        modifier = Modifier.padding(16.dp)
    )
}



@Composable
private fun CategoryChips(categories: List<String>, onPick: (String) -> Unit) {
    Row(
        modifier = Modifier
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        categories.forEach { c ->
            AssistChip(
                onClick = { onPick(c) },
                label = { Text(c) },
                leadingIcon = { Icon(Icons.Default.Star, contentDescription = null) }
            )
        }
    }
}



@Composable
private fun DestacadosRow(
    productos: List<com.dewis.dm_huertohogar_ex3.data.local.Product>,
    onOpen: (com.dewis.dm_huertohogar_ex3.data.local.Product) -> Unit,
    onAdd: (com.dewis.dm_huertohogar_ex3.data.local.Product) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(productos) { p ->
            Card(
                modifier = Modifier
                    .width(240.dp)
                    .clickable { onOpen(p) },
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(Modifier.padding(12.dp)) {
                    AsyncImage(
                        model = p.imageUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(p.name, style = MaterialTheme.typography.titleSmall)
                    Spacer(Modifier.height(4.dp))
                    Text("Precio: ${p.priceText}", color = MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.height(8.dp))
                    Button(
                        onClick = { onAdd(p) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.ShoppingCart, null)
                        Spacer(Modifier.width(6.dp))
                        Text("Agregar")
                    }
                }
            }
        }
    }
}



@Composable
private fun InfoBanner(
    title: String,
    bullets: List<String>,
    screenWidth: Dp
) {
    Surface(
        tonalElevation = 4.dp,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            bullets.forEach { b ->
                Text("• $b", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}



@Composable
private fun LocationsRow(cities: List<String>, openMaps: (String) -> Unit) {
    Row(
        Modifier
            .horizontalScroll(rememberScrollState())
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        cities.forEach { c ->
            Button(onClick = { openMaps(c) }) {
                Text(c)
            }
        }
    }
}
