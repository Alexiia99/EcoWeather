package data.mapper

import domain.model.*
import kotlin.math.roundToInt

class ForecastMapper(private val emoteMapper: LolEmoteMapper) {

    /**
     * 📅 Convierte respuesta de API en pronóstico de 5 días con emotes de LoL
     * VERSIÓN SIMPLE SIN LIBRERÍAS COMPLICADAS
     */
    fun mapToWeekForecast(
        forecastResponse: ForecastResponse,
        currentWeather: WeatherInfo? = null
    ): WeekForecast {
        // 📊 Agrupar mediciones por día (versión simple)
        val groupedByDay = forecastResponse.list.groupBy { item ->
            // Convertir timestamp a string de fecha simple
            val daysSinceEpoch = item.timestamp / 86400 // 86400 segundos en un día
            daysSinceEpoch.toString()
        }

        // 🎮 Crear pronóstico para cada día con emotes épicos
        val dayForecasts = groupedByDay.entries.take(5).mapIndexed { index, (dateKey, items) ->
            createDayForecast(index, items)
        }

        return WeekForecast(
            cityName = forecastResponse.city.name,
            country = forecastResponse.city.country,
            days = dayForecasts,
            todayWeather = currentWeather
        )
    }

    /**
     * 🌤️ Crea pronóstico para un día específico - VERSIÓN SIMPLE
     */
    private fun createDayForecast(dayIndex: Int, items: List<ForecastItem>): DayForecast {
        // 📊 Calcular estadísticas del día
        val temperatures = items.map { it.main.temp }
        val maxTemp = temperatures.maxOrNull() ?: 0.0
        val minTemp = temperatures.minOrNull() ?: 0.0
        val avgTemp = temperatures.average()

        // 💧 Promedios de otros datos
        val avgHumidity = items.map { it.main.humidity }.average().roundToInt()
        val avgWindSpeed = items.map { it.wind.speed }.average()

        // 📝 Descripción más común del día
        val descriptions = items.mapNotNull { it.weather.firstOrNull()?.description }
        val mostCommonDescription = descriptions.groupingBy { it }
            .eachCount()
            .maxByOrNull { it.value }?.key ?: "Variado"

        // 📅 Nombres de días simples
        val dayNames = listOf("Hoy", "Mañana", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo")
        val shortDates = listOf("Hoy", "Mañana", "3 Jul", "4 Jul", "5 Jul", "6 Jul", "7 Jul")

        val dayName = dayNames.getOrElse(dayIndex) { "Día ${dayIndex + 1}" }
        val shortDate = shortDates.getOrElse(dayIndex) { "${dayIndex + 3} Jul" }
        val fullDate = "$dayName ${dayIndex + 3} Jul"

        // 🎮 ¡Obtener emote de LoL para la temperatura promedio!
        val lolEmote = emoteMapper.getEmoteForTemperature(avgTemp)

        return DayForecast(
            date = fullDate,
            dayName = dayName,
            shortDate = shortDate,
            maxTemp = maxTemp,
            minTemp = minTemp,
            avgTemp = avgTemp,
            description = mostCommonDescription.replaceFirstChar { it.uppercase() },
            humidity = avgHumidity,
            windSpeed = avgWindSpeed,
            lolEmote = lolEmote,
            timestamp = items.first().timestamp,
            isToday = dayIndex == 0
        )
    }

    /**
     * 🎯 Convierte respuesta de clima actual a WeatherInfo (reutilizable)
     */
    fun mapToWeatherInfo(response: WeatherResponse): WeatherInfo {
        val tempCelsius = response.main.temp
        val lolEmote = emoteMapper.getEmoteForTemperature(tempCelsius)

        return WeatherInfo(
            temperature = tempCelsius,
            feelsLike = response.main.feelsLike,
            humidity = response.main.humidity,
            windSpeed = response.wind.speed,
            description = response.weather.firstOrNull()?.description?.replaceFirstChar { it.uppercase() } ?: "",
            cityName = response.name,
            lolEmote = lolEmote
        )
    }
}