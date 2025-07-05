package com.lolweather.android.presentation

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/**
 * 🎨 UTILIDADES COMPARTIDAS PARA EL CLIMA
 * ¡Funciones comunes para gradientes y colores!
 */
object WeatherUtils {

    /**
     * 🌈 Obtiene color según temperatura
     */
    fun getTemperatureColor(temperature: Double): Color {
        return when {
            temperature < 0 -> Color(0xFF2196F3)
            temperature < 10 -> Color(0xFF03DAC5)
            temperature < 20 -> Color(0xFF4CAF50)
            temperature < 30 -> Color(0xFFFFEB3B)
            temperature < 35 -> Color(0xFFFF9800)
            else -> Color(0xFFE53935)
        }
    }

    /**
     * 🎨 Obtiene gradiente de fondo según temperatura
     * ¡Usado tanto en WeatherScreen como ForecastScreen!
     */
    fun getGradientForTemperature(temperature: Double?): Brush {
        return when {
            temperature == null -> Brush.verticalGradient(
                colors = listOf(Color(0xFF2196F3), Color(0xFF21CBF3))
            )
            // ❄️ Congelándote (bajo 0°)
            temperature < 0 -> Brush.verticalGradient(
                colors = listOf(Color(0xFF0D47A1), Color(0xFF1976D2))
            )
            // 🧊 Frío (0-10°)
            temperature < 10 -> Brush.verticalGradient(
                colors = listOf(Color(0xFF1565C0), Color(0xFF42A5F5))
            )
            // 😊 Fresco (10-18°)
            temperature < 18 -> Brush.verticalGradient(
                colors = listOf(Color(0xFF0277BD), Color(0xFF29B6F6))
            )
            // 😎 Perfecto (18-25°)
            temperature < 25 -> Brush.verticalGradient(
                colors = listOf(Color(0xFF2E7D32), Color(0xFF66BB6A))
            )
            // 😅 Calorcito (25-32°)
            temperature < 32 -> Brush.verticalGradient(
                colors = listOf(Color(0xFFF57F17), Color(0xFFFFCA28))
            )
            // 🥵 Calor (32-38°)
            temperature < 38 -> Brush.verticalGradient(
                colors = listOf(Color(0xFFE65100), Color(0xFFFF9800))
            )
            // 🔥 ¡Te derrites! (38°+)
            else -> Brush.verticalGradient(
                colors = listOf(Color(0xFFD84315), Color(0xFFFF5722))
            )
        }
    }

    /**
     * 📅 Obtiene gradiente para pronóstico semanal
     */
    fun getWeekGradient(maxTemp: Double, minTemp: Double): Brush {
        val avgTemp = (maxTemp + minTemp) / 2
        return getGradientForTemperature(avgTemp)
    }

    /**
     * 🌡️ Formatea temperatura para mostrar
     */
    fun formatTemperature(temperature: Double): String {
        return "${temperature.toInt()}°"
    }

    /**
     * 📊 Normaliza altura para gráficos
     */
    fun normalizeTemperatureHeight(
        temperature: Double,
        minTemp: Double,
        maxTemp: Double,
        maxHeight: Double = 0.6
    ): Double {
        val tempRange = maxTemp - minTemp
        return if (tempRange > 0) {
            ((temperature - minTemp) / tempRange * maxHeight).coerceIn(0.1, maxHeight)
        } else {
            0.3
        }
    }

    /**
     * 🎮 Obtiene emoji de clima según temperatura
     * ¡Para cuando no tengamos imágenes de LoL!
     */
    fun getWeatherEmoji(temperature: Double): String {
        return when {
            temperature < 0 -> "🥶"   // Congelándote
            temperature < 10 -> "❄️"  // Frío
            temperature < 18 -> "😊"  // Fresco
            temperature < 25 -> "😎"  // Perfecto
            temperature < 32 -> "😅"  // Calorcito
            temperature < 38 -> "🥵"  // Calor
            else -> "🔥"             // Te derrites
        }
    }

    /**
     * 📝 Obtiene descripción épica según temperatura
     */
    fun getTemperatureDescription(temperature: Double): String {
        return when {
            temperature < 0 -> "¡Congelándote, campeón!"
            temperature < 10 -> "Frío de Freljord"
            temperature < 18 -> "Fresco como Ashe"
            temperature < 25 -> "¡Perfecto para jugar LoL!"
            temperature < 32 -> "Calorcito de Shurima"
            temperature < 38 -> "¡Calor de Brand!"
            else -> "¡Te derrites como un Poro!"
        }
    }

    /**
     * 🎯 Convierte velocidad del viento a descripción
     */
    fun getWindDescription(windSpeed: Double): String {
        return when {
            windSpeed < 5 -> "Calma"
            windSpeed < 15 -> "Brisa ligera"
            windSpeed < 25 -> "Brisa moderada"
            windSpeed < 40 -> "Viento fuerte"
            else -> "¡Ventolera épica!"
        }
    }

    /**
     * 💧 Convierte humedad a descripción
     */
    fun getHumidityDescription(humidity: Int): String {
        return when {
            humidity < 30 -> "Muy seco"
            humidity < 50 -> "Seco"
            humidity < 70 -> "Cómodo"
            humidity < 85 -> "Húmedo"
            else -> "Muy húmedo"
        }
    }

    /**
     * 🌡️ Convierte sensación térmica a descripción comparativa
     */
    fun getFeelsLikeDescription(actualTemp: Double, feelsLike: Double): String {
        val diff = feelsLike - actualTemp
        return when {
            diff > 5 -> "Se siente más caluroso"
            diff > 2 -> "Se siente un poco más cálido"
            diff < -5 -> "Se siente más frío"
            diff < -2 -> "Se siente un poco más frío"
            else -> "Se siente similar"
        }
    }

    /**
     * 📈 Obtiene el color del gráfico según el valor de temperatura
     * Versión específica para charts/gráficos
     */
    fun getChartTemperatureColor(temperature: Double): Color {
        return when {
            temperature < 0 -> Color(0xFF1976D2)   // Azul frío
            temperature < 10 -> Color(0xFF0288D1)  // Azul medio
            temperature < 18 -> Color(0xFF00ACC1)  // Cyan
            temperature < 25 -> Color(0xFF00C853)  // Verde perfecto
            temperature < 32 -> Color(0xFFFFC107)  // Amarillo cálido
            temperature < 38 -> Color(0xFFFF9800)  // Naranja calor
            else -> Color(0xFFE53935)              // Rojo extremo
        }
    }

    /**
     * 🎨 Obtiene gradiente radial para los emotes
     * Específico para componentes circulares
     */
    fun getRadialGradientForTemperature(temperature: Double): Brush {
        return when {
            temperature < 0 -> Brush.radialGradient(
                colors = listOf(
                    Color(0xFF42A5F5),
                    Color(0xFF1565C0)
                )
            )
            temperature < 10 -> Brush.radialGradient(
                colors = listOf(
                    Color(0xFF66BB6A),
                    Color(0xFF1976D2)
                )
            )
            temperature < 18 -> Brush.radialGradient(
                colors = listOf(
                    Color(0xFF81C784),
                    Color(0xFF0277BD)
                )
            )
            temperature < 25 -> Brush.radialGradient(
                colors = listOf(
                    Color(0xFF4CAF50),
                    Color(0xFF2E7D32)
                )
            )
            temperature < 32 -> Brush.radialGradient(
                colors = listOf(
                    Color(0xFFFFCA28),
                    Color(0xFFF57F17)
                )
            )
            temperature < 38 -> Brush.radialGradient(
                colors = listOf(
                    Color(0xFFFF9800),
                    Color(0xFFE65100)
                )
            )
            else -> Brush.radialGradient(
                colors = listOf(
                    Color(0xFFE53935),
                    Color(0xFFB71C1C)
                )
            )
        }
    }
}