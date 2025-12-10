package com.dewis.dm_huertohogar_ex3.ui.screens.cart

import android.app.Application
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

/**
 * Pruebas unitarias simples para CartViewModel.
 *
 * Probamos la lógica de validación de token, sin llamar a la API real.
 */
class CartViewModelTest {

    // App falsa solo para poder instanciar AndroidViewModel
    private class FakeApp : Application()

    private lateinit var vm: CartViewModel

    @Before
    fun setup() {
        vm = CartViewModel(FakeApp())
    }

    @Test
    fun `estado inicial no tiene error ni loading`() {
        val ui = vm.ui.value
        assertEquals(false, ui.loading)
        assertNull(ui.error)
        assertNull(ui.success)
        assertNull(ui.cart)
    }

    @Test
    fun `loadCart sin token debe mostrar error de sesion`() {
        vm.loadCart(null)

        val ui = vm.ui.value
        assertEquals("Debes iniciar sesión para ver el carrito.", ui.error)
    }

    @Test
    fun `addItem sin token debe mostrar error de sesion`() {
        vm.addItem(null, productoId = 1L, cantidad = 1)

        val ui = vm.ui.value
        assertEquals("Debes iniciar sesión para agregar productos.", ui.error)
    }

    @Test
    fun `removeItem sin token debe mostrar error de sesion`() {
        vm.removeItem(null, productoId = 1L)

        val ui = vm.ui.value
        assertEquals("Debes iniciar sesión para modificar el carrito.", ui.error)
    }

    @Test
    fun `checkout sin token debe mostrar error de sesion`() {
        vm.checkout(null)

        val ui = vm.ui.value
        assertEquals("Debes iniciar sesión para comprar.", ui.error)
    }
}
