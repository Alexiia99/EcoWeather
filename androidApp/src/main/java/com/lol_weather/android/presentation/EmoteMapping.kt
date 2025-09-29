package com.lolweather.android.presentation

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import kotlin.random.Random

/**
 * 🌱 Mapeo de tus GIFs eco-motivacionales por temperatura
 */
object EmoteMapping {

    /**
     * 🎬 Mapea temperatura a GIF animado principal (archivo en res/raw)
     */
    fun getAnimatedGifName(temperature: Double): String {
        return when {
            temperature < 0 -> "nieve"                    // ❄️ nieve.gif
            temperature < 10 -> "abrigo_lluvia"           // 🧥 abrigo_lluvia.gif
            temperature < 18 -> "bosque"                  // 🌳 bosque.gif
            temperature < 25 -> "planta_brotando"         // 🌱 planta_brotando.gif
            temperature < 32 -> "girasol"                 // 🌻 girasol.gif
            temperature < 38 -> "sol"                     // ☀️ sol.gif
            else -> "calentamiento_global"                // 🌡️ calentamiento_global.gif
        }
    }

    /**
     * 🎲 Mapeo ALEATORIO
     * Incluye alternativas para cada rango de temperatura
     */
    fun getRandomAnimatedGifName(temperature: Double): String {
        val gifs = when {
            // ❄️ Frío extremo (< 0°)
            temperature < 0 -> listOf("nieve")

            // 🧥 Frío (0-10°)
            temperature < 10 -> listOf("abrigo_lluvia", "bosque")

            // 🌳 Fresco (10-18°)
            temperature < 18 -> listOf("bosque", "flor_sakura")

            // 🌱 PERFECTO (18-25°)
            temperature < 25 -> listOf("planta_brotando", "bosque", "flor_sakura", "girasol")

            // 🌻 Cálido (25-32°)
            temperature < 32 -> listOf("girasol", "bosque", "sol")

            // ☀️ Calor (32-38°)
            temperature < 38 -> listOf("sol", "girasol")

            // 🌡️ Extremo (38°+)
            else -> listOf("calentamiento_global", "sol")
        }

        return gifs[Random.nextInt(gifs.size)]
    }

    /**
     * 🌍 Mapea temperatura a emoji eco-friendly (fallback si GIF no carga)
     */
    fun getEmoteEmoji(temperature: Double): String {
        return when {
            temperature < 0 -> "❄️"    // Nieve
            temperature < 10 -> "🧥"   // Abrigo para lluvia
            temperature < 18 -> "🌳"   // Bosque
            temperature < 25 -> "🌱"   // Planta brotando
            temperature < 32 -> "🌻"   // Girasol
            temperature < 38 -> "☀️"   // Sol
            else -> "🌡️"              // Calentamiento global
        }
    }

    /**
     * 🌈 Gradientes naturales que combinan con GIFs
     */
    fun getEmoteGradient(temperature: Double): Brush {
        return when {
            // ❄️ Azules fríos para nieve (< 0°)
            temperature < 0 -> Brush.radialGradient(
                colors = listOf(Color(0xFF81D4FA), Color(0xFF0277BD)) // Azul nieve
            )
            // 🧥 Azules grises para lluvia (0-10°)
            temperature < 10 -> Brush.radialGradient(
                colors = listOf(Color(0xFF90A4AE), Color(0xFF455A64)) // Gris lluvia
            )
            // 🌳 Verde bosque (10-18°)
            temperature < 18 -> Brush.radialGradient(
                colors = listOf(Color(0xFF81C784), Color(0xFF388E3C)) // Verde bosque
            )
            // 🌱 Verde natura
            temperature < 25 -> Brush.radialGradient(
                colors = listOf(Color(0xFF4CAF50), Color(0xFF2E7D32)) // Verde vibrante
            )
            // 🌻 Amarillo girasol (25-32°)
            temperature < 32 -> Brush.radialGradient(
                colors = listOf(Color(0xFFFFEB3B), Color(0xFFF57F17)) // Amarillo girasol
            )
            // ☀️ Naranja sol (32-38°)
            temperature < 38 -> Brush.radialGradient(
                colors = listOf(Color(0xFFFF9800), Color(0xFFE65100)) // Naranja sol
            )
            // 🌡️ Rojo alerta climática (38°+)
            else -> Brush.radialGradient(
                colors = listOf(Color(0xFFE57373), Color(0xFFD32F2F)) // Rojo calentamiento
            )
        }
    }

    /**
     * 📋 Lista completa de todos los GIFs disponibles
     */
    fun getAllAvailableGifs(): List<String> {
        return listOf(
            "nieve",                  // ❄️ Frío extremo
            "abrigo_lluvia",          // 🧥 Protección contra frío
            "bosque",                 // 🌳 Naturaleza
            "planta_brotando",        // 🌱 Crecimiento perfecto
            "flor_sakura",            // 🌸 Delicadeza natural
            "girasol",                // 🌻 Sol moderado
            "sol",                    // ☀️ Sol intenso
            "calentamiento_global"    // 🌡️ Alerta climática
        )
    }

    /**
     * 🎨 Colores temáticos que combinan con cada GIF
     */
    fun getGifThemeColor(gifName: String): Color {
        return when (gifName) {
            "nieve" -> Color(0xFF81D4FA)           // Azul nieve
            "abrigo_lluvia" -> Color(0xFF90A4AE)   // Gris lluvia
            "bosque" -> Color(0xFF4CAF50)          // Verde bosque
            "planta_brotando" -> Color(0xFF2E7D32) // Verde vida
            "flor_sakura" -> Color(0xFFE91E63)     // Rosa sakura
            "girasol" -> Color(0xFFFFEB3B)         // Amarillo girasol
            "sol" -> Color(0xFFFF9800)             // Naranja sol
            "calentamiento_global" -> Color(0xFFE57373) // Rojo alerta
            else -> Color(0xFF4CAF50)              // Verde por defecto
        }
    }

    /**
     * 🏷️ Descripción de cada GIF para debug/info
     */
    fun getGifDescription(gifName: String): String {
        return when (gifName) {
            "nieve" -> "Copos de nieve cayendo suavemente"
            "abrigo_lluvia" -> "Persona abrigándose para el frío"
            "bosque" -> "Bosque natural en armonía"
            "planta_brotando" -> "Nueva vida emergiendo de la tierra"
            "flor_sakura" -> "Flores de cerezo en primavera"
            "girasol" -> "Girasol siguiendo el sol"
            "sol" -> "Sol brillando intensamente"
            "calentamiento_global" -> "Termómetro indicando alerta climática"
            else -> "Animación del clima"
        }
    }

    /**
     * 📊 Estadísticas de tu colección de GIFs
     */
    fun getGifCollectionStats(): String {
        return "Colección eco-motivacional: ${getAllAvailableGifs().size} GIFs animados 🎬🌱\n" +
                "Cobertura completa de todos los rangos de temperatura"
    }

    /**
     * 🔄 IMPORTANTE: Lista de archivos que tienes funcionando
     */
    fun getWorkingGifs(): List<String> {
        return listOf(
            "nieve",
            "abrigo_lluvia",
            "bosque",
            "planta_brotando",
            "flor_sakura",
            "girasol",
            "sol",
            "calentamiento_global"
        )
    }
}