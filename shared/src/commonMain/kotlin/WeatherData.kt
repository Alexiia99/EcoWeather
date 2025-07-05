package domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherResponse(
    @SerialName("main") val main: MainWeather,
    @SerialName("weather") val weather: List<Weather>,
    @SerialName("wind") val wind: Wind,
    @SerialName("name") val name: String
)

@Serializable
data class MainWeather(
    @SerialName("temp") val temp: Double,
    @SerialName("feels_like") val feelsLike: Double,
    @SerialName("humidity") val humidity: Int,
    @SerialName("pressure") val pressure: Int
)

@Serializable
data class Weather(
    @SerialName("main") val main: String,
    @SerialName("description") val description: String,
    @SerialName("icon") val icon: String
)

@Serializable
data class Wind(
    @SerialName("speed") val speed: Double,
    @SerialName("deg") val deg: Int
)

// Domain model
data class WeatherInfo(
    val temperature: Double,
    val feelsLike: Double,
    val humidity: Int,
    val windSpeed: Double,
    val description: String,
    val cityName: String,
    val lolEmote: LolEmote
)

// LoL Emotes system
data class LolEmote(
    val name: String,
    val description: String,
    val imagePath: String,
    val temperatureRange: TemperatureRange
)

sealed class TemperatureRange(val min: Double, val max: Double, val description: String) {
    object FreezingCold : TemperatureRange(Double.NEGATIVE_INFINITY, 0.0, "Congelándote")
    object Cold : TemperatureRange(0.0, 10.0, "Frío")
    object Cool : TemperatureRange(10.0, 18.0, "Fresco")
    object Comfortable : TemperatureRange(18.0, 25.0, "Perfecto")
    object Warm : TemperatureRange(25.0, 32.0, "Calorcito")
    object Hot : TemperatureRange(32.0, 38.0, "Calor")
    object Scorching : TemperatureRange(38.0, Double.POSITIVE_INFINITY, "Te derrites")
}