package com.lolweather.android.presentation

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/**
 * 🛠️ Utilidades para el clima y UI - VERSIÓN ECO-MOTIVACIONAL COMPLETA
 * ¡Ahora integrado con tus GIFs y mensajes eco-friendly + funciones para gráficos!
 */
object WeatherUtils {

    /**
     * 🌡️ Formatea temperatura con grado
     */
    fun formatTemperature(temperature: Double): String {
        return "${temperature.toInt()}°"
    }

    /**
     * 🎨 Obtiene el gradiente según la temperatura - Colores más naturales
     */
    fun getGradientForTemperature(temperature: Double?): Brush {
        return when {
            temperature == null -> Brush.verticalGradient(
                colors = listOf(Color(0xFF2196F3), Color(0xFF21CBF3))
            )
            // ❄️ Azules fríos naturales (bajo 0°)
            temperature < 0 -> Brush.verticalGradient(
                colors = listOf(Color(0xFF0D47A1), Color(0xFF1976D2))
            )
            // 🧊 Azules suaves (0-10°)
            temperature < 10 -> Brush.verticalGradient(
                colors = listOf(Color(0xFF1565C0), Color(0xFF42A5F5))
            )
            // 🌳 Verde bosque (10-18°)
            temperature < 18 -> Brush.verticalGradient(
                colors = listOf(Color(0xFF388E3C), Color(0xFF81C784))
            )
            // 😎 Verde natura (18-25°) - TEMPERATURA IDEAL
            temperature < 25 -> Brush.verticalGradient(
                colors = listOf(Color(0xFF2E7D32), Color(0xFF66BB6A))
            )
            // 🌡️ Amarillo cálido (25-32°)
            temperature < 32 -> Brush.verticalGradient(
                colors = listOf(Color(0xFFF57F17), Color(0xFFFFCA28))
            )
            // 🥵 Naranja advertencia (32-38°)
            temperature < 38 -> Brush.verticalGradient(
                colors = listOf(Color(0xFFE65100), Color(0xFFFF9800))
            )
            // 🔥 Rojo alarma (38°+)
            else -> Brush.verticalGradient(
                colors = listOf(Color(0xFFD84315), Color(0xFFFF5722))
            )
        }
    }

    /**
     * 🌱 Obtiene emote eco-friendly para temperatura (fallback para GIFs)
     */
    fun getEmoteForTemperature(temperature: Double): String {
        return EmoteMapping.getEmoteEmoji(temperature)
    }

    /**
     * 🎬 NUEVO: Obtiene GIF animado para temperatura
     */
    fun getAnimatedIconName(temperature: Double): String {
        return EmoteMapping.getAnimatedGifName(temperature)
    }

    /**
     * 🎲 NUEVO: Obtiene GIF animado aleatorio para máxima variedad
     */
    fun getRandomAnimatedIconName(temperature: Double): String {
        return EmoteMapping.getRandomAnimatedGifName(temperature)
    }

    /**
     * 🌍 Nueva función principal: Descripción eco-motivacional aleatoria
     * ¡ESTA ES LA FUNCIÓN PRINCIPAL QUE USARÁS!
     */
    fun getEcoMotivationalDescription(temperature: Double): String {
        return EcoMotivationalMessages.getRandomMessage(temperature)
    }

    /**
     * 💚 Obtiene específicamente mensaje eco-friendly
     */
    fun getEcoDescription(temperature: Double): String {
        return EcoMotivationalMessages.getEcoMessage(temperature)
    }

    /**
     * 💪 Obtiene específicamente mensaje motivacional
     */
    fun getMotivationalDescription(temperature: Double): String {
        return EcoMotivationalMessages.getMotivationalMessage(temperature)
    }

    /**
     * 🌤️ Descripción del clima por temperatura (ACTUALIZADA)
     * Ahora usa el sistema eco-motivacional
     */
    fun getTemperatureDescription(temperature: Double): String {
        return getEcoMotivationalDescription(temperature)
    }

    /**
     * 🏷️ Obtiene etiqueta corta para la temperatura
     */
    fun getTemperatureLabel(temperature: Double): String {
        return when {
            temperature < 0 -> "Helando"
            temperature < 10 -> "Frío"
            temperature < 18 -> "Fresco"
            temperature < 25 -> "Perfecto"
            temperature < 32 -> "Cálido"
            temperature < 38 -> "Calor"
            else -> "Extremo"
        }
    }

    /**
     * 🎨 Obtiene color del texto según la temperatura
     */
    fun getTemperatureTextColor(temperature: Double): Color {
        return when {
            temperature < 0 -> Color(0xFF1976D2)   // Azul frío
            temperature < 10 -> Color(0xFF42A5F5)  // Azul claro
            temperature < 18 -> Color(0xFF4CAF50)  // Verde bosque
            temperature < 25 -> Color(0xFF66BB6A)  // Verde natural
            temperature < 32 -> Color(0xFFFFCA28)  // Amarillo cálido
            temperature < 38 -> Color(0xFFFF9800)  // Naranja
            else -> Color(0xFFFF5722)              // Rojo intenso
        }
    }

    /**
     * 🎨 Obtiene color específico para una temperatura (PARA GRÁFICOS)
     */
    fun getTemperatureColor(temperature: Double): Color {
        return when {
            temperature < 0 -> Color(0xFF1976D2)   // Azul intenso
            temperature < 10 -> Color(0xFF42A5F5)  // Azul medio
            temperature < 18 -> Color(0xFF4CAF50)  // Verde bosque
            temperature < 25 -> Color(0xFF8BC34A)  // Verde claro
            temperature < 32 -> Color(0xFFFFEB3B)  // Amarillo
            temperature < 38 -> Color(0xFFFF9800)  // Naranja
            else -> Color(0xFFE53935)              // Rojo
        }
    }

    /**
     * 📊 Normaliza altura de temperatura para gráficos (0.0 a 1.0)
     */
    fun normalizeTemperatureHeight(
        temperature: Double,
        minTemp: Double,
        maxTemp: Double
    ): Double {
        if (maxTemp == minTemp) return 0.5
        return ((temperature - minTemp) / (maxTemp - minTemp)).coerceIn(0.2, 0.9)
    }

    /**
     * 🌈 Gradiente para toda la semana basado en temperaturas extremas
     */
    fun getWeekGradient(maxWeekTemp: Double, minWeekTemp: Double): Brush {
        return when {
            // Semana muy fría
            maxWeekTemp < 10 -> Brush.verticalGradient(
                colors = listOf(Color(0xFF0D47A1), Color(0xFF1976D2))
            )
            // Semana fría
            maxWeekTemp < 18 -> Brush.verticalGradient(
                colors = listOf(Color(0xFF1565C0), Color(0xFF42A5F5))
            )
            // Semana perfecta
            maxWeekTemp < 25 -> Brush.verticalGradient(
                colors = listOf(Color(0xFF2E7D32), Color(0xFF66BB6A))
            )
            // Semana cálida
            maxWeekTemp < 32 -> Brush.verticalGradient(
                colors = listOf(Color(0xFFF57F17), Color(0xFFFFCA28))
            )
            // Semana caliente
            else -> Brush.verticalGradient(
                colors = listOf(Color(0xFFE65100), Color(0xFFFF5722))
            )
        }
    }

    /**
     * 📊 Información sobre el sistema de mensajes
     */
    fun getSystemInfo(): String {
        return EcoMotivationalMessages.getMessageStats()
    }

    /**
     * 🎬 Información sobre la colección de GIFs
     */
    fun getGifSystemInfo(): String {
        return EmoteMapping.getGifCollectionStats()
    }

    /**
     * 🔄 Función de compatibilidad con versiones anteriores
     * Redirige a la nueva función eco-motivacional
     */
    @Deprecated("Usa getEcoMotivationalDescription() en su lugar")
    fun getWeatherMood(temperature: Double): String {
        return getEcoMotivationalDescription(temperature)
    }
}