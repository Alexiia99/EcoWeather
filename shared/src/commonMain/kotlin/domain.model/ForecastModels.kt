package domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 📅 Respuesta de la API de pronóstico de 5 días
 */
@Serializable
data class ForecastResponse(
    @SerialName("list") val list: List<ForecastItem>,
    @SerialName("city") val city: ForecastCity
)

@Serializable
data class ForecastItem(
    @SerialName("dt") val timestamp: Long,
    @SerialName("main") val main: MainWeather,
    @SerialName("weather") val weather: List<Weather>,
    @SerialName("wind") val wind: Wind,
    @SerialName("dt_txt") val dateText: String
)

@Serializable
data class ForecastCity(
    @SerialName("name") val name: String,
    @SerialName("country") val country: String,
    @SerialName("coord") val coord: Coordinates
)

@Serializable
data class Coordinates(
    @SerialName("lat") val lat: Double,
    @SerialName("lon") val lon: Double
)

/**
 * 📊 Modelo de dominio para pronóstico de un día
 */
data class DayForecast(
    val date: String,              // "Lunes 8 Jul"
    val dayName: String,           // "Lunes", "Martes", etc.
    val shortDate: String,         // "8 Jul"
    val maxTemp: Double,           // Temperatura máxima
    val minTemp: Double,           // Temperatura mínima
    val avgTemp: Double,           // Temperatura promedio para el emote
    val description: String,       // "Soleado", "Nublado", etc.
    val humidity: Int,             // Humedad promedio
    val windSpeed: Double,         // Velocidad del viento
    val lolEmote: LolEmote,        // 🎮 Emote de LoL para este día
    val timestamp: Long,           // Para ordenar
    val isToday: Boolean = false   // Si es el día actual
)

/**
 * 📈 Modelo para el pronóstico completo de 5 días
 */
data class WeekForecast(
    val cityName: String,
    val country: String,
    val days: List<DayForecast>,
    val todayWeather: WeatherInfo? = null // Clima actual detallado
) {
    /**
     * 📊 Obtiene las temperaturas para el gráfico
     */
    val temperatureData: List<Pair<String, Double>>
        get() = days.map { it.dayName.take(3) to it.avgTemp }

    /**
     * 🎮 Obtiene todos los emotes de la semana
     */
    val weekEmotes: List<LolEmote>
        get() = days.map { it.lolEmote }

    /**
     * 🌡️ Temperatura máxima de la semana
     */
    val maxWeekTemp: Double
        get() = days.maxOfOrNull { it.maxTemp } ?: 0.0

    /**
     * 🌡️ Temperatura mínima de la semana
     */
    val minWeekTemp: Double
        get() = days.minOfOrNull { it.minTemp } ?: 0.0
}