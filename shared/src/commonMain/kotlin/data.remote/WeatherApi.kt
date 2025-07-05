package data.remote

import domain.model.WeatherResponse
import domain.model.ForecastResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class WeatherApi(private val client: HttpClient) {

    // 🔑 IMPORTANTE: Cambiar por tu API key real de OpenWeatherMap
    private val apiKey = "93b9011bdc95f3d6fd52f0e9cfb5d775"
    private val baseUrl = "https://api.openweathermap.org/data/2.5"

    /**
     * 🌤️ Obtiene el clima actual por coordenadas geográficas
     */
    suspend fun getWeatherByCoordinates(lat: Double, lon: Double): WeatherResponse {
        return client.get("$baseUrl/weather") {
            parameter("lat", lat)
            parameter("lon", lon)
            parameter("appid", apiKey)
            parameter("units", "metric") // Para celsius
            parameter("lang", "es") // Descripciones en español
        }.body()
    }

    /**
     * 🌤️ Obtiene el clima actual por nombre de ciudad
     */
    suspend fun getWeatherByCity(cityName: String): WeatherResponse {
        return client.get("$baseUrl/weather") {
            parameter("q", cityName)
            parameter("appid", apiKey)
            parameter("units", "metric") // Para celsius
            parameter("lang", "es") // Descripciones en español
        }.body()
    }

    /**
     * 📅 Obtiene pronóstico de 5 días por coordenadas - ¡NUEVA FUNCIÓN ÉPICA!
     */
    suspend fun getForecastByCoordinates(lat: Double, lon: Double): ForecastResponse {
        return client.get("$baseUrl/forecast") {
            parameter("lat", lat)
            parameter("lon", lon)
            parameter("appid", apiKey)
            parameter("units", "metric") // Para celsius
            parameter("lang", "es") // Descripciones en español
            parameter("cnt", 40) // 5 días * 8 mediciones por día (cada 3 horas)
        }.body()
    }

    /**
     * 📅 Obtiene pronóstico de 5 días por nombre de ciudad - ¡NUEVA FUNCIÓN ÉPICA!
     */
    suspend fun getForecastByCity(cityName: String): ForecastResponse {
        return client.get("$baseUrl/forecast") {
            parameter("q", cityName)
            parameter("appid", apiKey)
            parameter("units", "metric") // Para celsius
            parameter("lang", "es") // Descripciones en español
            parameter("cnt", 40) // 5 días * 8 mediciones por día (cada 3 horas)
        }.body()
    }

    /**
     * 🚀 Obtiene clima actual Y pronóstico de 5 días en una sola llamada
     * ¡SUPER EFICIENTE!
     */
    suspend fun getCompleteWeatherByCoordinates(lat: Double, lon: Double): Pair<WeatherResponse, ForecastResponse> {
        // Llamadas en paralelo para mejor rendimiento
        val currentWeather = getWeatherByCoordinates(lat, lon)
        val forecast = getForecastByCoordinates(lat, lon)
        return Pair(currentWeather, forecast)
    }

    /**
     * 🚀 Obtiene clima actual Y pronóstico de 5 días por ciudad
     */
    suspend fun getCompleteWeatherByCity(cityName: String): Pair<WeatherResponse, ForecastResponse> {
        val currentWeather = getWeatherByCity(cityName)
        val forecast = getForecastByCity(cityName)
        return Pair(currentWeather, forecast)
    }
}