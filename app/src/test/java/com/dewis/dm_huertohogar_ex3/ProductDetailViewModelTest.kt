package com.dewis.dm_huertohogar_ex3.ui.screens.detail

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

/**
 * Pruebas unitarias para ProductDetailViewModel.
 *
 * Probamos que, sin token, no se llame al backend y se muestre
 * el mensaje de error correcto.
 */
class ProductDetailViewModelTest {

    private lateinit var vm: ProductDetailViewModel

    @Before
    fun setup() {
        vm = ProductDetailViewModel()
    }

    @Test
    fun `estado inicial no esta cargando ni tiene error ni producto`() {
        val ui = vm.ui
        assertFalse(ui.loading)
        assertNull(ui.error)
        assertNull(ui.product)
    }

    @Test
    fun `load con token nulo debe dejar mensaje de error y no cargar`() {
        vm.load(token = null, id = 1L)

        val ui = vm.ui
        assertEquals("Debes iniciar sesión para ver el detalle.", ui.error)
        assertFalse(ui.loading)
        assertNull(ui.product)
    }

    @Test
    fun `load con token en blanco debe dejar mensaje de error y no cargar`() {
        vm.load(token = "   ", id = 1L)

        val ui = vm.ui
        assertEquals("Debes iniciar sesión para ver el detalle.", ui.error)
        assertFalse(ui.loading)
        assertNull(ui.product)
    }
}
