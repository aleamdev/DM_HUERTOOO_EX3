package com.dewis.dm_huertohogar_ex3.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dewis.dm_huertohogar_ex3.ui.screens.auth.LoginScreen
import com.dewis.dm_huertohogar_ex3.ui.screens.home.HomeScreen
import com.dewis.dm_huertohogar_ex3.ui.screens.catalog.CatalogScreen
import com.dewis.dm_huertohogar_ex3.ui.screens.detail.ProductDetailScreen
import com.dewis.dm_huertohogar_ex3.ui.screens.device.CameraScreen
import com.dewis.dm_huertohogar_ex3.ui.screens.device.LocationScreen
import com.dewis.dm_huertohogar_ex3.ui.screens.cart.CartScreen

object Routes {
    const val LOGIN = "login"
    const val HOME = "home"
    const val CATALOG = "catalog"
    const val DETAIL = "detail/{productId}"
    const val CAMERA = "camera"
    const val LOCATION = "location"
    const val CART = "cart"

    fun catalogRoute(q: String? = null, cat: String? = null): String {
        val params = buildList {
            if (!q.isNullOrBlank()) add("q=${Uri.encode(q)}")
            if (!cat.isNullOrBlank()) add("cat=${Uri.encode(cat)}")
        }
        return if (params.isEmpty()) CATALOG else "$CATALOG?${params.joinToString("&")}"
    }
}

@Composable
fun AppNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(navController, startDestination = Routes.LOGIN) {

        composable(Routes.LOGIN) {
            LoginScreen(
                onLoggedIn = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                goToRegister = { }
            )
        }

        composable(Routes.HOME) {
            HomeScreen(
                onSearch = { query ->
                    navController.navigate(Routes.catalogRoute(q = query))
                },
                onSelect = { key ->
                    when {
                        key == "home" -> navController.navigate(Routes.HOME) {
                            popUpTo(Routes.HOME) { inclusive = true }
                        }
                        key == "catalog" -> navController.navigate(Routes.CATALOG)
                        key.startsWith("catalog:") -> {
                            val cat = key.removePrefix("catalog:")
                            navController.navigate(Routes.catalogRoute(cat = cat))
                        }
                        key == "camera" -> navController.navigate(Routes.CAMERA)
                        key == "location" -> navController.navigate(Routes.LOCATION)
                        key == "cart" -> navController.navigate(Routes.CART)
                        key == "login" -> navController.navigate(Routes.LOGIN) { popUpTo(0) }
                        key == "logout" -> navController.navigate(Routes.LOGIN) { popUpTo(0) }
                    }
                }
            )
        }

        composable(
            route = "${Routes.CATALOG}?q={q}&cat={cat}",
            arguments = listOf(
                navArgument("q"){ nullable = true; defaultValue = null },
                navArgument("cat"){ nullable = true; defaultValue = null }
            )
        ) { backStack ->
            val q   = backStack.arguments?.getString("q")
            val cat = backStack.arguments?.getString("cat")
            CatalogScreen(
                q = q,
                cat = cat,
                goToDetail = { id -> navController.navigate("detail/$id") },
                goToCamera = { navController.navigate(Routes.CAMERA) },
                goToLocation = { navController.navigate(Routes.LOCATION) }
            )
        }

        composable(Routes.DETAIL) { back ->
            val id = back.arguments?.getString("productId") ?: "0"
            ProductDetailScreen(productId = id)
        }

        composable(Routes.CAMERA)   { CameraScreen() }
        composable(Routes.LOCATION) { LocationScreen() }
        composable(Routes.CART)     { CartScreen(onBack = { navController.popBackStack() }) }
    }
}
