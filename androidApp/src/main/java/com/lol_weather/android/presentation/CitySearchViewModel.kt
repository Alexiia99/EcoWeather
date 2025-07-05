package com.lolweather.android.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * 🧠 ViewModel para la búsqueda de ciudades
 * ¡Gestiona búsquedas, favoritos y ciudades populares!
 */
class CitySearchViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CitySearchUiState())
    val uiState: StateFlow<CitySearchUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    init {
        loadPopularCities()
    }

    /**
     * 🔍 Busca ciudades con debounce
     */
    fun searchCities(query: String) {
        // Cancelar búsqueda anterior
        searchJob?.cancel()

        if (query.length < 2) {
            _uiState.value = _uiState.value.copy(
                searchResults = emptyList(),
                isSearching = false
            )
            return
        }

        searchJob = viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSearching = true)

            // Debounce - esperar 500ms antes de buscar
            delay(500)

            try {
                val results = performCitySearch(query)
                _uiState.value = _uiState.value.copy(
                    searchResults = results,
                    isSearching = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    searchResults = emptyList(),
                    isSearching = false
                )
            }
        }
    }

    /**
     * 🔍 Búsqueda real de ciudades (simulada con datos locales)
     */
    private fun performCitySearch(query: String): List<CityItem> {
        // Base de datos local de ciudades populares
        val allCities = getAllWorldCities()

        return allCities.filter { city ->
            city.name.contains(query, ignoreCase = true) ||
                    city.country.contains(query, ignoreCase = true)
        }.take(20) // Máximo 20 resultados
    }

    /**
     * ❌ Limpia la búsqueda
     */
    fun clearSearch() {
        searchJob?.cancel()
        _uiState.value = _uiState.value.copy(
            searchResults = emptyList(),
            isSearching = false
        )
    }

    /**
     * ⭐ Agrega ciudad a favoritos
     */
    fun addToFavorites(city: CityItem) {
        val currentFavorites = _uiState.value.favoriteCities.toMutableList()
        val updatedCity = city.copy(isFavorite = !city.isFavorite)

        if (updatedCity.isFavorite) {
            if (!currentFavorites.any { it.name == city.name }) {
                currentFavorites.add(updatedCity)
            }
        } else {
            currentFavorites.removeAll { it.name == city.name }
        }

        _uiState.value = _uiState.value.copy(
            favoriteCities = currentFavorites,
            // Actualizar en resultados de búsqueda también
            searchResults = _uiState.value.searchResults.map {
                if (it.name == city.name) updatedCity else it
            }
        )
    }

    /**
     * 🌟 Carga ciudades populares
     */
    private fun loadPopularCities() {
        val popularCities = getPopularCities()
        _uiState.value = _uiState.value.copy(
            popularCities = popularCities
        )
    }

    /**
     * 🌟 Lista de ciudades populares
     */
    private fun getPopularCities(): List<CityItem> {
        return listOf(
            // 🇪🇸 España
            CityItem("Madrid", "España", emoji = "🇪🇸"),
            CityItem("Barcelona", "España", emoji = "🇪🇸"),
            CityItem("Sevilla", "España", emoji = "🇪🇸"),
            CityItem("Valencia", "España", emoji = "🇪🇸"),
            CityItem("Bilbao", "España", emoji = "🇪🇸"),

            // 🌍 Mundo
            CityItem("London", "Reino Unido", emoji = "🇬🇧"),
            CityItem("Paris", "Francia", emoji = "🇫🇷"),
            CityItem("Rome", "Italia", emoji = "🇮🇹"),
            CityItem("Berlin", "Alemania", emoji = "🇩🇪"),
            CityItem("Amsterdam", "Países Bajos", emoji = "🇳🇱"),
            CityItem("Tokyo", "Japón", emoji = "🇯🇵"),
            CityItem("New York", "Estados Unidos", emoji = "🇺🇸"),
            CityItem("Los Angeles", "Estados Unidos", emoji = "🇺🇸"),
            CityItem("Sydney", "Australia", emoji = "🇦🇺"),
            CityItem("Dubai", "Emiratos Árabes Unidos", emoji = "🇦🇪")
        )
    }

    /**
     * 🌍 Base de datos completa de ciudades del mundo
     */
    private fun getAllWorldCities(): List<CityItem> {
        return listOf(
            // 🇪🇸 España - Completa
            CityItem("Madrid", "España", emoji = "🇪🇸"),
            CityItem("Barcelona", "España", emoji = "🇪🇸"),
            CityItem("Sevilla", "España", emoji = "🇪🇸"),
            CityItem("Valencia", "España", emoji = "🇪🇸"),
            CityItem("Zaragoza", "España", emoji = "🇪🇸"),
            CityItem("Málaga", "España", emoji = "🇪🇸"),
            CityItem("Murcia", "España", emoji = "🇪🇸"),
            CityItem("Palma", "España", emoji = "🇪🇸"),
            CityItem("Las Palmas", "España", emoji = "🇪🇸"),
            CityItem("Bilbao", "España", emoji = "🇪🇸"),
            CityItem("Alicante", "España", emoji = "🇪🇸"),
            CityItem("Córdoba", "España", emoji = "🇪🇸"),
            CityItem("Valladolid", "España", emoji = "🇪🇸"),
            CityItem("Vigo", "España", emoji = "🇪🇸"),
            CityItem("Gijón", "España", emoji = "🇪🇸"),
            CityItem("Granada", "España", emoji = "🇪🇸"),
            CityItem("Vitoria", "España", emoji = "🇪🇸"),
            CityItem("Santiago de Compostela", "España", emoji = "🇪🇸"),
            CityItem("Pamplona", "España", emoji = "🇪🇸"),
            CityItem("Toledo", "España", emoji = "🇪🇸"),

            // 🇫🇷 Francia
            CityItem("Paris", "Francia", emoji = "🇫🇷"),
            CityItem("Lyon", "Francia", emoji = "🇫🇷"),
            CityItem("Marseille", "Francia", emoji = "🇫🇷"),
            CityItem("Nice", "Francia", emoji = "🇫🇷"),
            CityItem("Bordeaux", "Francia", emoji = "🇫🇷"),
            CityItem("Toulouse", "Francia", emoji = "🇫🇷"),
            CityItem("Cannes", "Francia", emoji = "🇫🇷"),

            // 🇮🇹 Italia
            CityItem("Rome", "Italia", emoji = "🇮🇹"),
            CityItem("Milan", "Italia", emoji = "🇮🇹"),
            CityItem("Naples", "Italia", emoji = "🇮🇹"),
            CityItem("Venice", "Italia", emoji = "🇮🇹"),
            CityItem("Florence", "Italia", emoji = "🇮🇹"),
            CityItem("Turin", "Italia", emoji = "🇮🇹"),

            // 🇬🇧 Reino Unido
            CityItem("London", "Reino Unido", emoji = "🇬🇧"),
            CityItem("Manchester", "Reino Unido", emoji = "🇬🇧"),
            CityItem("Birmingham", "Reino Unido", emoji = "🇬🇧"),
            CityItem("Liverpool", "Reino Unido", emoji = "🇬🇧"),
            CityItem("Edinburgh", "Reino Unido", emoji = "🇬🇧"),
            CityItem("Glasgow", "Reino Unido", emoji = "🇬🇧"),

            // 🇩🇪 Alemania
            CityItem("Berlin", "Alemania", emoji = "🇩🇪"),
            CityItem("Munich", "Alemania", emoji = "🇩🇪"),
            CityItem("Hamburg", "Alemania", emoji = "🇩🇪"),
            CityItem("Cologne", "Alemania", emoji = "🇩🇪"),
            CityItem("Frankfurt", "Alemania", emoji = "🇩🇪"),

            // 🇺🇸 Estados Unidos
            CityItem("New York", "Estados Unidos", emoji = "🇺🇸"),
            CityItem("Los Angeles", "Estados Unidos", emoji = "🇺🇸"),
            CityItem("Chicago", "Estados Unidos", emoji = "🇺🇸"),
            CityItem("Miami", "Estados Unidos", emoji = "🇺🇸"),
            CityItem("San Francisco", "Estados Unidos", emoji = "🇺🇸"),
            CityItem("Las Vegas", "Estados Unidos", emoji = "🇺🇸"),
            CityItem("Seattle", "Estados Unidos", emoji = "🇺🇸"),
            CityItem("Boston", "Estados Unidos", emoji = "🇺🇸"),

            // 🇯🇵 Japón
            CityItem("Tokyo", "Japón", emoji = "🇯🇵"),
            CityItem("Osaka", "Japón", emoji = "🇯🇵"),
            CityItem("Kyoto", "Japón", emoji = "🇯🇵"),
            CityItem("Hiroshima", "Japón", emoji = "🇯🇵"),

            // 🇦🇺 Australia
            CityItem("Sydney", "Australia", emoji = "🇦🇺"),
            CityItem("Melbourne", "Australia", emoji = "🇦🇺"),
            CityItem("Brisbane", "Australia", emoji = "🇦🇺"),
            CityItem("Perth", "Australia", emoji = "🇦🇺"),

            // 🇨🇦 Canadá
            CityItem("Toronto", "Canadá", emoji = "🇨🇦"),
            CityItem("Vancouver", "Canadá", emoji = "🇨🇦"),
            CityItem("Montreal", "Canadá", emoji = "🇨🇦"),

            // 🇲🇽 México
            CityItem("Mexico City", "México", emoji = "🇲🇽"),
            CityItem("Cancun", "México", emoji = "🇲🇽"),
            CityItem("Guadalajara", "México", emoji = "🇲🇽"),

            // 🇧🇷 Brasil
            CityItem("Rio de Janeiro", "Brasil", emoji = "🇧🇷"),
            CityItem("São Paulo", "Brasil", emoji = "🇧🇷"),
            CityItem("Salvador", "Brasil", emoji = "🇧🇷"),

            // 🇦🇷 Argentina
            CityItem("Buenos Aires", "Argentina", emoji = "🇦🇷"),
            CityItem("Córdoba", "Argentina", emoji = "🇦🇷"),

            // 🇨🇳 China
            CityItem("Beijing", "China", emoji = "🇨🇳"),
            CityItem("Shanghai", "China", emoji = "🇨🇳"),
            CityItem("Hong Kong", "China", emoji = "🇭🇰"),

            // 🇮🇳 India
            CityItem("Mumbai", "India", emoji = "🇮🇳"),
            CityItem("New Delhi", "India", emoji = "🇮🇳"),
            CityItem("Bangalore", "India", emoji = "🇮🇳"),

            // 🇦🇪 Emiratos Árabes Unidos
            CityItem("Dubai", "Emiratos Árabes Unidos", emoji = "🇦🇪"),
            CityItem("Abu Dhabi", "Emiratos Árabes Unidos", emoji = "🇦🇪"),

            // 🇷🇺 Rusia
            CityItem("Moscow", "Rusia", emoji = "🇷🇺"),
            CityItem("Saint Petersburg", "Rusia", emoji = "🇷🇺"),

            // 🇰🇷 Corea del Sur
            CityItem("Seoul", "Corea del Sur", emoji = "🇰🇷"),
            CityItem("Busan", "Corea del Sur", emoji = "🇰🇷"),

            // 🇸🇬 Singapur
            CityItem("Singapore", "Singapur", emoji = "🇸🇬"),

            // 🇹🇭 Tailandia
            CityItem("Bangkok", "Tailandia", emoji = "🇹🇭"),
            CityItem("Phuket", "Tailandia", emoji = "🇹🇭"),

            // Más ciudades...
            CityItem("Istanbul", "Turquía", emoji = "🇹🇷"),
            CityItem("Cairo", "Egipto", emoji = "🇪🇬"),
            CityItem("Cape Town", "Sudáfrica", emoji = "🇿🇦"),
            CityItem("Reykjavik", "Islandia", emoji = "🇮🇸"),
            CityItem("Stockholm", "Suecia", emoji = "🇸🇪"),
            CityItem("Oslo", "Noruega", emoji = "🇳🇴"),
            CityItem("Copenhagen", "Dinamarca", emoji = "🇩🇰"),
            CityItem("Helsinki", "Finlandia", emoji = "🇫🇮"),
            CityItem("Zurich", "Suiza", emoji = "🇨🇭"),
            CityItem("Vienna", "Austria", emoji = "🇦🇹"),
            CityItem("Prague", "República Checa", emoji = "🇨🇿"),
            CityItem("Budapest", "Hungría", emoji = "🇭🇺"),
            CityItem("Warsaw", "Polonia", emoji = "🇵🇱"),
            CityItem("Lisbon", "Portugal", emoji = "🇵🇹"),
            CityItem("Athens", "Grecia", emoji = "🇬🇷")
        )
    }
}

/**
 * 📱 Estado de la UI de búsqueda de ciudades
 */
data class CitySearchUiState(
    val searchResults: List<CityItem> = emptyList(),
    val popularCities: List<CityItem> = emptyList(),
    val favoriteCities: List<CityItem> = emptyList(),
    val isSearching: Boolean = false
)