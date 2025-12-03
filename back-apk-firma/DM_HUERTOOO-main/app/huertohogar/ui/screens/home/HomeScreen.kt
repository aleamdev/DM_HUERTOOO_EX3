package com.dewis.huertohogar.ui.screens.home

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.dewis.huertohogar.ui.components.AppTopBar
import com.dewis.huertohogar.ui.session.SessionViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

private val BrandGreen = Color(0xFF2E8B57)
@Composable private fun ChipShape() = MaterialTheme.shapes.large


@Composable
fun HomeScreen(
    onSearch: (String) -> Unit,
    onSelect: (String) -> Unit
) {
    val ctx = LocalContext.current
    val sessionVm: SessionViewModel = viewModel()
    val email by sessionVm.email.collectAsState(initial = null)

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
        Column(
            Modifier
                .padding(pad)
                .fillMaxSize()
        ) {

            HeroSection(
                title = "Frescura del campo a tu mesa",
                subtitle = "Explora frutas, verduras y productos orgánicos de calidad",
                imageUrl = "https://images.unsplash.com/photo-1461354464878-ad92f492a5a0",
                ctaText = "Ver catálogo",
                onClickCta = { onSelect("catalog") }
            )

            CategoryChips(
                categories = listOf("Frutas frescas", "Verduras orgánicas", "Productos orgánicos", "Productos lácteos"),
                onPick = { cat -> onSelect("catalog:$cat") }
            )

            SectionTitle("Productos Destacados")
            val destacados = listOf(
                FeaturedCardData("Frutas Destacadas", "$1,200", "$900 CLP por kilo",
                    "https://images.unsplash.com/photo-1542838132-92c53300491e"),
                FeaturedCardData("Verduras Frescas", "$700", "$550 por bolsa de 500g",
                    "https://images.unsplash.com/photo-1506806732259-39c2d0268443"),
                FeaturedCardData("Lácteos", "$1,200", "$980 CLP por litro",
                    "https://images.unsplash.com/photo-1580910051074-3eb694886505")
            )
            FeaturedRow(data = destacados, onAdd = { onSelect("catalog") })

            InfoBanner(
                title = "Productos locales y orgánicos",
                bullets = listOf(
                    "Seleccionados de productores locales",
                    "Sin pesticidas, frescura garantizada",
                    "Envíos a todo Santiago"
                )
            )

            SectionTitle("Del Blog")
            BlogRow(
                posts = listOf(
                    BlogCard("Cómo elegir frutas de temporada", "https://tu-blog-o-url"),
                    BlogCard("Beneficios de consumir orgánico", "https://tu-blog-o-url"),
                    BlogCard("Recetas rápidas y saludables", "https://tu-blog-o-url")
                ),
                open = { url -> ctx.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url))) }
            )

            SectionTitle("Nuestras Ubicaciones")
            LocationsRow(
                cities = listOf("Santiago", "Valparaíso", "Rancagua", "San Bernardo"),
                openMaps = { city ->
                    val url = "https://maps.google.com/?q=${Uri.encode("HuertoHogar $city")}"
                    ctx.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                }
            )

            if (!email.isNullOrBlank()) {
                Spacer(Modifier.height(8.dp))
                TextButton(
                    onClick = { sessionVm.logout { onSelect("logout") } },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) { Text("Cerrar sesión") }
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

// Secciones

@Composable
private fun HeroSection(
    title: String,
    subtitle: String,
    imageUrl: String,
    ctaText: String,
    onClickCta: () -> Unit
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
                        colors = listOf(Color(0xAA000000), Color.Transparent, Color(0xAA000000))
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
                Text(
                    title,
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.SemiBold),
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
                Text(
                    subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }
            Button(
                onClick = onClickCta,
                colors = ButtonDefaults.buttonColors(containerColor = BrandGreen)
            ) { Text(ctaText) }
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Surface(
        color = BrandGreen,
        contentColor = Color.White,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp, start = 12.dp, end = 12.dp)
    ) {
        Text(
            text,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
        )
    }
}

@Composable
private fun CategoryChips(
    categories: List<String>,
    onPick: (String) -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        categories.forEach { c ->
            OutlinedButton(
                onClick = { onPick(c) },
                shape = ChipShape(),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = BrandGreen)
            ) { Text(c) }
        }
    }
}

//destacados

private data class FeaturedCardData(
    val title: String,
    val oldPrice: String,
    val price: String,
    val image: String
)

@Composable
private fun FeaturedRow(
    data: List<FeaturedCardData>,
    onAdd: () -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(data) { item ->
            Card(
                modifier = Modifier.width(240.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(Modifier.padding(12.dp)) {
                    AsyncImage(
                        model = item.image,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(item.title, style = MaterialTheme.typography.titleSmall)
                    Text(
                        item.oldPrice,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray,
                        textDecoration = TextDecoration.LineThrough
                    )
                    Text(
                        item.price,
                        style = MaterialTheme.typography.bodyMedium,
                        color = BrandGreen
                    )
                    Button(
                        onClick = onAdd,
                        modifier = Modifier.padding(top = 8.dp)
                    ) { Text("Agregar al carrito") }
                }
            }
        }
    }
}

//informativo

@Composable
private fun InfoBanner(
    title: String,
    bullets: List<String>
) {
    Surface(
        tonalElevation = 2.dp,
        shape = MaterialTheme.shapes.large,
        modifier = Modifier
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium, color = BrandGreen)
            Spacer(Modifier.height(6.dp))
            bullets.forEach { b ->
                Text("• $b", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

//blog

private data class BlogCard(val title: String, val url: String)

@Composable
private fun BlogRow(posts: List<BlogCard>, open: (String) -> Unit) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(posts) { post ->
            Card(
                onClick = { open(post.url) },
                modifier = Modifier.width(240.dp)
            ) {
                Column(Modifier.padding(12.dp)) {
                    Text(post.title, style = MaterialTheme.typography.titleSmall)
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Leer más",
                        style = MaterialTheme.typography.labelLarge,
                        color = BrandGreen
                    )
                }
            }
        }
    }
}

//ubicaciones

@Composable
private fun LocationsRow(cities: List<String>, openMaps: (String) -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        cities.forEach { city ->
            Button(
                onClick = { openMaps(city) },
                shape = ChipShape(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
            ) { Text(city) }
        }
    }
}
