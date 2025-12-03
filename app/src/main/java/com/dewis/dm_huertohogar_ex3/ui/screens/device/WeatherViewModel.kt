package com.dewis.dm_huertohogar_ex3.ui.screens.device

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dewis.dm_huertohogar_ex3.data.remote.ApiClient
import com.dewis.dm_huertohogar_ex3.data.remote.WeatherResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class WeatherUiState(
    val loading: Boolean = false,
    val error: String? = null,
    val data: WeatherResponse? = null
)

class WeatherViewModel : ViewModel() {

    private val _ui = MutableStateFlow(WeatherUiState())
    val ui: StateFlow<WeatherUiState> = _ui

    fun loadWeather(lat: Double, lon: Double, key: String) {
        viewModelScope.launch {
            try {
                _ui.value = WeatherUiState(loading = true)
                val resp = ApiClient.weatherApi.getWeather(lat, lon, key)
                _ui.value = WeatherUiState(loading = false, data = resp)
            } catch (e: Exception) {
                _ui.value = WeatherUiState(
                    loading = false,
                    error = "No se pudo obtener el clima"
                )
            }
        }
    }
}
