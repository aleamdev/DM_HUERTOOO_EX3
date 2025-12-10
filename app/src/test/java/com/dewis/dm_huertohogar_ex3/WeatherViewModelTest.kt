package com.dewis.dm_huertohogar_ex3.ui.screens.device

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

/**
 * Pruebas unitarias para WeatherViewModel.
 *
 * Por simplicidad solo validamos el estado inicial, sin llamar a la API externa.
 */
class WeatherViewModelTest {

    private lateinit var vm: WeatherViewModel

    @Before
    fun setup() {
        vm = WeatherViewModel()
    }

    @Test
    fun `estado inicial no esta cargando y no tiene datos ni error`() {
        val ui = vm.ui.value

        assertFalse(ui.loading)
        assertNull(ui.error)
        assertNull(ui.data)
    }
}
