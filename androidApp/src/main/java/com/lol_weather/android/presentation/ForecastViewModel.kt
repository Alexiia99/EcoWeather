package com.lolweather.android.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lolweather.android.location.LocationManager
import domain.model.WeekForecast
import domain.usecase.GetCompleteWeatherUseCase
import domain.usecase.GetForecastUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * 🧠 ViewModel para el pronóstico de 5 días
 * ¡Gestiona todo el estado épico del pronóstico!
 */
class ForecastViewModel(
    private val getForecastUseCase: GetForecastUseCase,
    private val getCompleteWeatherUseCase: GetCompleteWeatherUseCase,
    private val locationManager: LocationManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(ForecastUiState())
    val uiState: StateFlow<ForecastUiState> = _uiState.asStateFlow()

    init {
        // 🚀 Cargar pronóstico automáticamente al iniciar
        loadForecastWithLocation()
    }

    /**
     * 📍 Carga el pronóstico usando geolocalización automática
     * Si falla, usa Valencia como fallback
     */
    private fun loadForecastWithLocation() {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)

        viewModelScope.launch {
            try {
                // Intentar obtener ubicación actual
                val location = locationManager.getCurrentLocation()

                if (location != null) {
                    // 🎯 Usar ubicación real del usuario
                    getCompleteWeatherByLocation(location.first, location.second)
                } else {
                    // 🏠 Fallback a Valencia si no se puede obtener ubicación
                    getCompleteWeatherByCity("Valencia")
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Error al obtener ubicación: ${e.message}"
                )
            }
        }
    }

    /**
     * 🌍 Obtiene pronóstico completo (clima actual + 5 días) por ubicación
     */
    fun getCompleteWeatherByLocation(lat: Double, lon: Double) {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)

        viewModelScope.launch {
            getCompleteWeatherUseCase.byLocation(lat, lon)
                .onSuccess { weekForecast ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        weekForecast = weekForecast,
                        error = null
                    )
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message ?: "Error al obtener pronóstico completo"
                    )
                }
        }
    }

    /**
     * 🏙️ Obtiene pronóstico completo por nombre de ciudad
     */
    fun getCompleteWeatherByCity(cityName: String) {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)

        viewModelScope.launch {
            getCompleteWeatherUseCase.byCity(cityName)
                .onSuccess { weekForecast ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        weekForecast = weekForecast,
                        error = null
                    )
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message ?: "Error al obtener pronóstico completo"
                    )
                }
        }
    }

    /**
     * 📅 Obtiene SOLO pronóstico de 5 días (sin clima actual) por ubicación
     */
    fun getForecastByLocation(lat: Double, lon: Double) {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)

        viewModelScope.launch {
            getForecastUseCase.byLocation(lat, lon)
                .onSuccess { weekForecast ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        weekForecast = weekForecast,
                        error = null
                    )
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message ?: "Error al obtener pronóstico"
                    )
                }
        }
    }

    /**
     * 🏙️ Obtiene SOLO pronóstico de 5 días por ciudad
     */
    fun getForecastByCity(cityName: String) {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)

        viewModelScope.launch {
            getForecastUseCase.byCity(cityName)
                .onSuccess { weekForecast ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        weekForecast = weekForecast,
                        error = null
                    )
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message ?: "Error al obtener pronóstico"
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
     * 🔄 Refresca el pronóstico - intenta con ubicación primero
     */
    fun refresh() {
        loadForecastWithLocation()
    }

    /**
     * 📍 Fuerza actualización con ubicación
     */
    fun refreshWithLocation() {
        loadForecastWithLocation()
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
 * 📱 Estado de la UI del pronóstico
 * Contiene toda la información necesaria para la pantalla
 */
data class ForecastUiState(
    val isLoading: Boolean = false,
    val weekForecast: WeekForecast? = null,
    val error: String? = null
) {
    /**
     * 🎮 Indica si tenemos datos del pronóstico para mostrar
     */
    val hasForecastData: Boolean
        get() = weekForecast != null && !isLoading

    /**
     * 📊 Obtiene el número de días disponibles
     */
    val availableDays: Int
        get() = weekForecast?.days?.size ?: 0

    /**
     * 🌡️ Obtiene la temperatura máxima de la semana
     */
    val maxWeekTemperature: Double?
        get() = weekForecast?.maxWeekTemp

    /**
     * 🌡️ Obtiene la temperatura mínima de la semana
     */
    val minWeekTemperature: Double?
        get() = weekForecast?.minWeekTemp

    /**
     * 🎭 Obtiene todos los emotes de la semana
     */
    val weekEmotes: List<String>
        get() = weekForecast?.weekEmotes?.map { it.name } ?: emptyList()

    /**
     * 📈 Obtiene los datos para el gráfico de temperaturas
     */
    val temperatureChartData: List<Pair<String, Double>>
        get() = weekForecast?.temperatureData ?: emptyList()

    /**
     * 🏙️ Obtiene el nombre de la ciudad
     */
    val cityName: String?
        get() = weekForecast?.cityName

    /**
     * 🌍 Obtiene el país
     */
    val country: String?
        get() = weekForecast?.country

    /**
     * 📅 Obtiene el pronóstico de hoy si está disponible
     */
    val todayForecast: domain.model.DayForecast?
        get() = weekForecast?.days?.firstOrNull { it.isToday }

    /**
     * 🌤️ Obtiene el clima actual detallado si está disponible
     */
    val currentWeather: domain.model.WeatherInfo?
        get() = weekForecast?.todayWeather
}