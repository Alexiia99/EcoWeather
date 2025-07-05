package domain.repository

import domain.model.WeatherInfo
import domain.model.WeekForecast

interface WeatherRepository {
    /**
     * 🌤️ Obtiene información del clima actual por ubicación geográfica
     */
    suspend fun getWeatherByLocation(lat: Double, lon: Double): Result<WeatherInfo>

    /**
     * 🌤️ Obtiene información del clima actual por nombre de ciudad
     */
    suspend fun getWeatherByCity(cityName: String): Result<WeatherInfo>

    /**
     * 📅 Obtiene pronóstico de 5 días por ubicación geográfica
     */
    suspend fun getForecastByLocation(lat: Double, lon: Double): Result<WeekForecast>

    /**
     * 📅 Obtiene pronóstico de 5 días por nombre de ciudad
     */
    suspend fun getForecastByCity(cityName: String): Result<WeekForecast>

    /**
     * 🚀 Obtiene clima actual Y pronóstico en una sola llamada
     */
    suspend fun getCompleteWeatherByLocation(lat: Double, lon: Double): Result<WeekForecast>

    /**
     * 🚀 Obtiene clima actual Y pronóstico por ciudad
     */
    suspend fun getCompleteWeatherByCity(cityName: String): Result<WeekForecast>
}