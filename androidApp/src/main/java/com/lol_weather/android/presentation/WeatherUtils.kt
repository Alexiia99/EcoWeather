package com.lolweather.android.presentation

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/**
 * 🛠️ Utilidades para el clima y UI
 */
object WeatherUtils {

    /**
     * 🌡️ Formatea temperatura con grado
     */
    fun formatTemperature(temperature: Double): String {
        return "${temperature.toInt()}°"
    }

    /**
     * 🎨 Obtiene el gradiente según la temperatura
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
     * 🎮 Obtiene emote para temperatura (compatible con tus emotes LoL)
     */
    fun getEmoteForTemperature(temperature: Double): String {
        return when {
            temperature < 0 -> "🐧"    // Poro Helado
            temperature < 10 -> "❄️"   // Frío
            temperature < 18 -> "😊"   // Fresco
            temperature < 25 -> "😎"   // Perfecto
            temperature < 32 -> "😅"   // Calorcito
            temperature < 38 -> "🥵"   // Calor
            else -> "🔥"              // Te derrites
        }
    }

    /**
     * 🌤️ Descripción del clima por temperatura
     */
    fun getTemperatureDescription(temperature: Double): String {
        return when {
            temperature < 0 -> "¡Congelándote!"
            temperature < 10 -> "Frío pero manejable"
            temperature < 18 -> "Clima perfecto para salir"
            temperature < 25 -> "¡Temperatura ideal!"
            temperature < 32 -> "Empezando a sudar..."
            temperature < 38 -> "¡Qué calor hace!"
            else -> "¡Me estoy derritiendo!"
        }
    }

    /**
     * 📊 Normaliza la altura de temperatura para gráficos (0.0 a 1.0)
     */
    fun normalizeTemperatureHeight(
        temperature: Double,
        minTemp: Double,
        maxTemp: Double
    ): Double {
        if (maxTemp == minTemp) return 0.5
        return ((temperature - minTemp) / (maxTemp - minTemp)).coerceIn(0.2, 1.0)
    }

    /**
     * 🎨 Color según temperatura para gráficos
     */
    fun getTemperatureColor(temperature: Double): Color {
        return when {
            temperature < 0 -> Color(0xFF3B82F6)   // Azul frío
            temperature < 10 -> Color(0xFF0EA5E9)  // Azul claro
            temperature < 18 -> Color(0xFF10B981)  // Verde
            temperature < 25 -> Color(0xFF22C55E)  // Verde claro
            temperature < 32 -> Color(0xFFF59E0B)  // Amarillo/naranja
            temperature < 38 -> Color(0xFFEF4444)  // Rojo
            else -> Color(0xFFDC2626)              // Rojo intenso
        }
    }

    /**
     * 🌈 Gradiente para semana completa
     */
    fun getWeekGradient(maxWeekTemp: Double, minWeekTemp: Double): Brush {
        val avgTemp = (maxWeekTemp + minWeekTemp) / 2.0
        return getGradientForTemperature(avgTemp)
    }
}