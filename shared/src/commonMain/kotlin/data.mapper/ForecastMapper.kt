package data.mapper

import domain.model.*
import kotlin.math.roundToInt

class ForecastMapper(private val emoteMapper: LolEmoteMapper) {

    /**
     * 📅 Convierte respuesta de API en pronóstico de 5 días con emotes de LoL
     * VERSIÓN CORREGIDA - Agrupa correctamente por fecha local
     */
    fun mapToWeekForecast(
        forecastResponse: ForecastResponse,
        currentWeather: WeatherInfo? = null
    ): WeekForecast {

        // 🔧 CORRECCIÓN: Agrupar por fecha real, no por timestamp
        val groupedByDay = forecastResponse.list.groupBy { item ->
            // Convertir timestamp a fecha local usando dateText que viene de la API
            // dateText format: "2025-07-05 15:00:00"
            val dateOnly = item.dateText.substringBefore(" ") // Obtener solo "2025-07-05"
            dateOnly
        }

        // 📊 Tomar solo los primeros 5 días y ordenarlos por fecha
        val sortedDays = groupedByDay.entries
            .sortedBy { it.key } // Ordenar por fecha
            .take(5)

        // 🎮 Crear pronóstico para cada día con emotes épicos
        val dayForecasts = sortedDays.mapIndexed { index, (dateKey, items) ->
            createDayForecast(index, dateKey, items)
        }

        return WeekForecast(
            cityName = forecastResponse.city.name,
            country = forecastResponse.city.country,
            days = dayForecasts,
            todayWeather = currentWeather
        )
    }

    /**
     * 🌤️ Crea pronóstico para un día específico - VERSIÓN ESPECÍFICA PARA VALENCIA
     */
    private fun createDayForecast(dayIndex: Int, dateKey: String, items: List<ForecastItem>): DayForecast {

        // 🔥 SOLUCIÓN ESPECÍFICA: Buscar temperatura máxima entre 12:00-15:00
        val maxTempItems = items.filter { item ->
            val hour = item.dateText.substringAfter(" ").substringBefore(":").toIntOrNull() ?: 12
            hour in 12..15 // Horas de máximo calor
        }

        // 🌙 Buscar temperatura mínima entre 03:00-06:00
        val minTempItems = items.filter { item ->
            val hour = item.dateText.substringAfter(" ").substringBefore(":").toIntOrNull() ?: 6
            hour in 3..6 // Horas de mínimo frío
        }

        // Si no tenemos datos específicos, usar todo el día
        val dataForMax = if (maxTempItems.isNotEmpty()) maxTempItems else items
        val dataForMin = if (minTempItems.isNotEmpty()) minTempItems else items

        // 🌡️ Calcular temperaturas ESPECÍFICAMENTE
        val maxTemp = dataForMax.map { it.main.temp }.maxOrNull() ?: 0.0
        val minTemp = dataForMin.map { it.main.temp }.minOrNull() ?: 0.0
        val avgTemp = (maxTemp + minTemp) / 2.0 // Promedio simple más realista

        // 💧 Promedios de otros datos usando todos los items del día
        val avgHumidity = items.map { it.main.humidity }.average().roundToInt()
        val avgWindSpeed = items.map { it.wind.speed }.average()

        // 📝 Descripción más común del día (de las horas diurnas)
        val descriptions = dataForMax.mapNotNull { it.weather.firstOrNull()?.description }
        val mostCommonDescription = descriptions.groupingBy { it }
            .eachCount()
            .maxByOrNull { it.value }?.key ?: "Variado"

        // 📅 CORRECCIÓN: Nombres de días más precisos
        val (dayName, shortDate, fullDate) = generateDayNames(dayIndex, dateKey)

        // 🎮 ¡Obtener emote de LoL para la temperatura MÁXIMA del día!
        val lolEmote = emoteMapper.getEmoteForTemperature(maxTemp)

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
     * 📅 Genera nombres de días más precisos
     */
    private fun generateDayNames(dayIndex: Int, dateKey: String): Triple<String, String, String> {
        val dayNames = when (dayIndex) {
            0 -> "Hoy"
            1 -> "Mañana"
            else -> {
                // Aquí podrías usar una librería de fechas para obtener el día real
                // Por ahora, usar nombres genéricos
                val weekDays = listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo")
                weekDays[dayIndex % 7]
            }
        }

        // Extraer día y mes del dateKey (2025-07-05 -> 5 Jul)
        val dateParts = dateKey.split("-")
        val monthNames = listOf("", "Ene", "Feb", "Mar", "Abr", "May", "Jun",
            "Jul", "Ago", "Sep", "Oct", "Nov", "Dic")
        val day = dateParts.getOrNull(2)?.toIntOrNull() ?: dayIndex + 5
        val month = dateParts.getOrNull(1)?.toIntOrNull() ?: 7
        val monthName = monthNames.getOrNull(month) ?: "Jul"

        val shortDate = "$day $monthName"
        val fullDate = "$dayNames $shortDate"

        return Triple(dayNames, shortDate, fullDate)
    }

    /**
     * 🎯 Convierte respuesta de clima actual a WeatherInfo (sin cambios)
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