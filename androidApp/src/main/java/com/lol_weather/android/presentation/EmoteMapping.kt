package com.lolweather.android.presentation

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/**
 * 🎮 Mapeo de emotes por temperatura
 * ¡AQUÍ PONDREMOS TUS NOMBRES DE ARCHIVOS!
 */
object EmoteMapping {

    /**
     * 🖼️ Mapea temperatura a nombre de imagen (archivo en res/drawable)
     */
    fun getImageResourceName(temperature: Double): String? {
        return when {
            temperature < 0 -> "porohelado"          // 🥶 Poro congelado para frío extremo
            temperature < 10 -> "nevar"              // ❄️ Nevar para frío
            temperature < 18 -> "fresco"             // 😊 Fresco para clima fresco
            temperature < 25 -> "tiempoperfecto"     // 😎 Tiempo perfecto para clima ideal
            temperature < 32 -> "calor"              // 😅 Calor para empezar a hacer calor
            temperature < 38 -> "muchocalor"         // 🥵 Mucho calor para calor fuerte
            else -> "muuchocalor"                    // 😵 Muuucho calor para calor extremo
        }
    }

    /**
     * 🎭 Mapea temperatura a emoji de fallback
     */
    fun getEmoteEmoji(temperature: Double): String {
        return when {
            temperature < 0 -> "🐧"    // Poro helado
            temperature < 10 -> "❄️"   // Nevar
            temperature < 18 -> "😊"   // Fresco
            temperature < 25 -> "😎"   // Tiempo perfecto
            temperature < 32 -> "😅"   // Calor
            temperature < 38 -> "🥵"   // Mucho calor
            else -> "🔥"              // Muuucho calor
        }
    }

    /**
     * 🌈 Mapea temperatura a gradiente colorido
     */
    fun getEmoteGradient(temperature: Double): Brush {
        return when {
            temperature < 0 -> Brush.radialGradient(
                colors = listOf(Color(0xFF1565C0), Color(0xFF0D47A1))
            )
            temperature < 10 -> Brush.radialGradient(
                colors = listOf(Color(0xFF42A5F5), Color(0xFF1976D2))
            )
            temperature < 18 -> Brush.radialGradient(
                colors = listOf(Color(0xFF29B6F6), Color(0xFF0277BD))
            )
            temperature < 25 -> Brush.radialGradient(
                colors = listOf(Color(0xFF66BB6A), Color(0xFF2E7D32))
            )
            temperature < 32 -> Brush.radialGradient(
                colors = listOf(Color(0xFFFFCA28), Color(0xFFF57F17))
            )
            temperature < 38 -> Brush.radialGradient(
                colors = listOf(Color(0xFFFF9800), Color(0xFFE65100))
            )
            else -> Brush.radialGradient(
                colors = listOf(Color(0xFFE53935), Color(0xFFB71C1C))
            )
        }
    }
}