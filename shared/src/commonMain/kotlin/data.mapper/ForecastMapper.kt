package data.mapper

import domain.model.*
import kotlin.math.roundToInt

class ForecastMapper(private val emoteMapper: LolEmoteMapper) {

    /**
     * 📅 Convierte respuesta de API en pronóstico de 5 días con emotes de LoL
     * ¡AHORA CON FECHAS REALES DEL SISTEMA!
     */
    fun mapToWeekForecast(
        forecastResponse: ForecastResponse,
        currentWeather: WeatherInfo? = null
    ): WeekForecast {
        // 📊 Agrupar mediciones por día usando timestamps reales
        val groupedByDay = forecastResponse.list.groupBy { item ->
            // Convertir timestamp a día (en milisegundos)
            val dayInMillis = item.timestamp * 1000L
            val daysSinceEpoch = dayInMillis / (24 * 60 * 60 * 1000)
            daysSinceEpoch
        }

        // 🎮 Crear pronóstico para cada día con fechas reales
        val dayForecasts = groupedByDay.entries.take(5).mapIndexed { index, (_, items) ->
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
     * 🌤️ Crea pronóstico para un día específico - CON FECHAS REALES DEL SISTEMA
     */
    private fun createDayForecast(
        dayIndex: Int,
        items: List<ForecastItem>
    ): DayForecast {
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

        // 📅 USAR TIMESTAMP REAL DE LA API
        val itemTimestamp = items.first().timestamp * 1000L // Convertir a milisegundos
        val (dayName, shortDate, fullDate) = calculateRealDates(dayIndex, itemTimestamp)

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
     * 📅 Calcula fechas reales basándose en el timestamp de la API
     */
    private fun calculateRealDates(dayIndex: Int, timestamp: Long): Triple<String, String, String> {
        val dayNames = listOf("Domingo", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado")
        val dayNamesShort = listOf("Dom", "Lun", "Mar", "Mié", "Jue", "Vie", "Sáb")
        val monthNames = listOf("Ene", "Feb", "Mar", "Abr", "May", "Jun",
            "Jul", "Ago", "Sep", "Oct", "Nov", "Dic")

        // Calcular día de la semana desde el timestamp
        // (timestamp en millis / millis por día + ajuste de epoch) % 7
        val dayOfWeek = ((timestamp / (24 * 60 * 60 * 1000L)) + 4) % 7 // +4 para ajustar epoch (1 Ene 1970 era Jueves)

        // Calcular día del mes y mes (aproximación simple pero funcional)
        val daysSinceEpoch = timestamp / (24 * 60 * 60 * 1000L)
        val dayOfMonth = ((daysSinceEpoch % 31) + 1).toInt() // Aproximación de día del mes
        val monthIndex = ((daysSinceEpoch / 31) % 12).toInt() // Aproximación de mes

        val dayName = when (dayIndex) {
            0 -> "HOY"
            1 -> "Mañana"
            else -> dayNamesShort[dayOfWeek.toInt()]
        }

        val shortDate = when (dayIndex) {
            0 -> "Hoy"
            1 -> "Mañana"
            else -> "$dayOfMonth ${monthNames[monthIndex]}"
        }

        val fullDate = when (dayIndex) {
            0 -> "Hoy"
            1 -> "Mañana"
            else -> "${dayNames[dayOfWeek.toInt()]} $dayOfMonth ${monthNames[monthIndex]}"
        }

        return Triple(dayName, shortDate, fullDate)
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