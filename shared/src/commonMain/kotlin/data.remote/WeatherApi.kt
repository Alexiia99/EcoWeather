package data.remote

import domain.model.WeatherResponse
import domain.model.ForecastResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class WeatherApi(private val client: HttpClient) {

    // 🔑 Tu API key actual de OpenWeatherMap
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
            parameter("units", "metric")
            parameter("lang", "es")
        }.body()
    }

    /**
     * 🌤️ Obtiene el clima actual por nombre de ciudad
     */
    suspend fun getWeatherByCity(cityName: String): WeatherResponse {
        return client.get("$baseUrl/weather") {
            parameter("q", cityName)
            parameter("appid", apiKey)
            parameter("units", "metric")
            parameter("lang", "es")
        }.body()
    }

    /**
     * 📅 Obtiene pronóstico de 5 días por coordenadas - CON VALIDACIÓN
     */
    suspend fun getForecastByCoordinates(lat: Double, lon: Double): ForecastResponse {
        val response = client.get("$baseUrl/forecast") {
            parameter("lat", lat)
            parameter("lon", lon)
            parameter("appid", apiKey)
            parameter("units", "metric")
            parameter("lang", "es")
            parameter("cnt", 40)
        }.body<ForecastResponse>()

        // 🔍 DEBUG MEJORADO
        println("🌡️ === VALIDANDO DATOS DE API ===")
        response.list.take(12).forEach { item ->
            val fecha = item.dateText
            val temp = item.main.temp.toInt()
            val hora = fecha.substringAfter(" ").substringBefore(":")
            println("📅 $fecha -> $temp°C (hora: $hora)")
        }

        // 🛡️ VALIDACIÓN DE ANOMALÍAS
        val validatedResponse = validateTemperatureData(response)
        return validatedResponse
    }

    /**
     * 📅 Obtiene pronóstico de 5 días por nombre de ciudad - CON VALIDACIÓN
     */
    suspend fun getForecastByCity(cityName: String): ForecastResponse {
        val response = client.get("$baseUrl/forecast") {
            parameter("q", cityName)
            parameter("appid", apiKey)
            parameter("units", "metric")
            parameter("lang", "es")
            parameter("cnt", 40)
        }.body<ForecastResponse>()

        // 🔍 DEBUG MEJORADO
        println("🌡️ === VALIDANDO DATOS DE API ===")
        response.list.take(12).forEach { item ->
            val fecha = item.dateText
            val temp = item.main.temp.toInt()
            val hora = fecha.substringAfter(" ").substringBefore(":")
            println("📅 $fecha -> $temp°C (hora: $hora)")
        }

        // 🛡️ VALIDACIÓN DE ANOMALÍAS
        val validatedResponse = validateTemperatureData(response)
        return validatedResponse
    }

    /**
     * 🚀 Obtiene clima actual Y pronóstico de 5 días en una sola llamada
     */
    suspend fun getCompleteWeatherByCoordinates(lat: Double, lon: Double): Pair<WeatherResponse, ForecastResponse> {
        val currentWeather = getWeatherByCoordinates(lat, lon)
        val forecast = getForecastByCoordinates(lat, lon)
        return Pair(currentWeather, forecast)
    }

    /**
     * 🚀 Obtiene clima completo por ciudad
     */
    suspend fun getCompleteWeatherByCity(cityName: String): Pair<WeatherResponse, ForecastResponse> {
        val currentWeather = getWeatherByCity(cityName)
        val forecast = getForecastByCity(cityName)
        return Pair(currentWeather, forecast)
    }

    /**
     * 🛡️ NUEVA FUNCIÓN - Valida y corrige anomalías de temperatura
     */
    private fun validateTemperatureData(response: ForecastResponse): ForecastResponse {
        println("🛡️ === INICIANDO VALIDACIÓN DE TEMPERATURAS ===")

        // Agrupar por días para validar mejor
        val groupedByDay = response.list.groupBy { item ->
            item.dateText.substringBefore(" ")
        }

        val correctedItems = mutableListOf<domain.model.ForecastItem>()
        var previousDayMaxTemp: Double? = null

        groupedByDay.entries.forEachIndexed { dayIndex, (date, items) ->
            println("📅 Validando día: $date")

            // Obtener máxima del día actual
            val currentMaxTemp = items.map { it.main.temp }.maxOrNull() ?: 0.0

            // Si hay día anterior, validar el salto
            val correctedMaxTemp = if (previousDayMaxTemp != null) {
                val tempDifference = kotlin.math.abs(currentMaxTemp - previousDayMaxTemp!!)

                if (tempDifference > 6.0) {
                    println("⚠️ ANOMALÍA DETECTADA: Salto de ${tempDifference.toInt()}°C")
                    println("   Día anterior: ${previousDayMaxTemp!!.toInt()}°C")
                    println("   Día actual: ${currentMaxTemp.toInt()}°C")

                    // Corrección inteligente: interpolar gradualmente
                    val correctedTemp = if (currentMaxTemp > previousDayMaxTemp!!) {
                        // Si sube mucho, limitarlo a +3°C
                        previousDayMaxTemp!! + 3.0
                    } else {
                        // Si baja mucho, limitarlo a -3°C
                        kotlin.math.max(previousDayMaxTemp!! - 3.0, currentMaxTemp)
                    }

                    println("   ✅ Temperatura corregida: ${correctedTemp.toInt()}°C")
                    correctedTemp
                } else {
                    println("✅ Temperatura normal: ${currentMaxTemp.toInt()}°C")
                    currentMaxTemp
                }
            } else {
                println("✅ Primer día: ${currentMaxTemp.toInt()}°C")
                currentMaxTemp
            }

            // Aplicar corrección proporcionalmente a todos los items del día
            val correctionFactor = if (currentMaxTemp != 0.0) correctedMaxTemp / currentMaxTemp else 1.0

            val correctedDayItems = items.map { item ->
                val originalTemp = item.main.temp
                val correctedTemp = originalTemp * correctionFactor

                item.copy(
                    main = item.main.copy(
                        temp = correctedTemp,
                        feelsLike = item.main.feelsLike * correctionFactor
                    )
                )
            }

            correctedItems.addAll(correctedDayItems)
            previousDayMaxTemp = correctedMaxTemp
        }

        println("🏁 === VALIDACIÓN COMPLETADA ===")

        return response.copy(list = correctedItems)
    }
}