package com.lolweather.android.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lolweather.android.location.LocationManager
import domain.model.WeatherInfo
import domain.usecase.GetWeatherUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * 🧠 ViewModel que gestiona el estado de la pantalla del clima
 * Ahora con geolocalización automática! 📍
 */
class WeatherViewModel(
    private val getWeatherUseCase: GetWeatherUseCase,
    private val locationManager: LocationManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(WeatherUiState())
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    init {
        // 📍 Intentar obtener ubicación automáticamente al iniciar
        loadWeatherWithLocation()
    }

    /**
     * 📍 Carga el clima usando geolocalización automática
     * Si falla, usa Valencia como fallback
     */
    private fun loadWeatherWithLocation() {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)

        viewModelScope.launch {
            // Intentar obtener ubicación actual
            val location = locationManager.getCurrentLocation()

            if (location != null) {
                // 🎯 Usar ubicación real del usuario
                getWeatherByLocation(location.first, location.second)
            } else {
                // 🏠 Fallback a Valencia si no se puede obtener ubicación
                getWeatherByCity("Valencia")
            }
        }
    }

    /**
     * 🏙️ Obtiene el clima por nombre de ciudad
     * @param cityName Nombre de la ciudad (ej: "Madrid", "Barcelona")
     */
    fun getWeatherByCity(cityName: String) {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)

        viewModelScope.launch {
            getWeatherUseCase.byCity(cityName)
                .onSuccess { weatherInfo ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        weatherInfo = weatherInfo,
                        error = null
                    )
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message ?: "Error al obtener el clima"
                    )
                }
        }
    }

    /**
     * 📍 Obtiene el clima por ubicación geográfica
     * @param lat Latitud
     * @param lon Longitud
     */
    fun getWeatherByLocation(lat: Double, lon: Double) {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)

        viewModelScope.launch {
            getWeatherUseCase.byLocation(lat, lon)
                .onSuccess { weatherInfo ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        weatherInfo = weatherInfo,
                        error = null
                    )
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message ?: "Error al obtener el clima"
                    )
                }
        }
    }

    /**
     * ❌ Limpia el error actual
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    /**
     * 🔄 Refresca el clima - intenta con ubicación primero
     */
    fun refresh() {
        loadWeatherWithLocation()
    }

    /**
     * 📍 Fuerza actualización con ubicación
     */
    fun refreshWithLocation() {
        loadWeatherWithLocation()
    }

    /**
     * 🔐 Verifica si tenemos permisos de ubicación
     */
    fun hasLocationPermission(): Boolean {
        return locationManager.hasLocationPermission()
    }

    /**
     * 📱 Obtiene permisos necesarios para solicitar
     */
    fun getRequiredPermissions(): Array<String> {
        return locationManager.getRequiredPermissions()
    }
}

/**
 * 📱 Estado de la UI del clima
 * Contiene toda la información necesaria para la pantalla
 */
data class WeatherUiState(
    val isLoading: Boolean = false,
    val weatherInfo: WeatherInfo? = null,
    val error: String? = null
) {
    /**
     * 🎮 Indica si tenemos datos del clima para mostrar
     */
    val hasWeatherData: Boolean
        get() = weatherInfo != null && !isLoading

    /**
     * 🎨 Obtiene la temperatura actual o null
     */
    val currentTemperature: Double?
        get() = weatherInfo?.temperature

    /**
     * 🎭 Obtiene el emote actual de LoL o null
     */
    val currentLolEmote: String?
        get() = weatherInfo?.lolEmote?.name
}