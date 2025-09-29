package com.lolweather.android.presentation

import kotlin.random.Random

/**
 * 🌱 Sistema de mensajes motivacionales y eco-friendly para la app del tiempo
 * Cada rango de temperatura tiene múltiples frases inspiradoras
 */
object EcoMotivationalMessages {

    /**
     * 🧊 Frases para temperatura < 0°C - Frío extremo
     */
    private val freezingMessages = listOf(
        "Es día de chocolate calentito ☕",
        "Te recomiendo una buena peli y manta 🛋️",
        "Que el viento helado refresque tus ideas y te inspire a tener un gran día ❄️",
        "El frío nos recuerda que la Tierra también necesita descansar 🌍",
        "Aprovecha para reducir tu huella de carbono: menos aire acondicionado 💚",
        "Los días fríos son perfectos para leer sobre sostenibilidad 📚",
        "Cada grado importa: apreciemos estos momentos naturales 🌿"
    )

    /**
     * ❄️ Frases para temperatura < 10°C - Frío
     */
    private val coldMessages = listOf(
        "No olvides llevarte una sudadera 🧥",
        "Si no tuviéramos invierno, la primavera no resultaría tan agradable 🌸",
        "El frío nos conecta con la naturaleza en su estado más puro 🍃",
        "Aprovecha para usar ropa abrigada y ahorrar energía en casa ⚡",
        "Un día perfecto para caminar y reducir las emisiones de tu vehículo 🚶‍♀️",
        "La naturaleza se regenera en el frío, igual que tu energía ♻️",
        "Cada paso que das hoy cuida el planeta 🌱"
    )

    /**
     * 🌤️ Frases para temperatura < 18°C - Fresco
     */
    private val coolMessages = listOf(
        "Recuerda tus 15 minutos de sol ☀️",
        "Con el buen tiempo aprovecha para pasear y despejarte 🚶",
        "La temperatura perfecta para conectar con la naturaleza 🌳",
        "Abre las ventanas y deja que el aire fresco renueve tu hogar 🏠",
        "Un día ideal para usar bicicleta y cuidar el medio ambiente 🚲",
        "La brisa natural es el mejor aire acondicionado 🍃",
        "Aprovecha para plantar algo verde hoy 🌱"
    )

    /**
     * 😎 Frases para temperatura < 25°C - Perfecto
     */
    private val perfectMessages = listOf(
        "Temperatura perfecta para dar un buen paseo 🚶‍♂️",
        "El clima ideal existe, y es hoy 🌈",
        "Aprovecha este regalo de la naturaleza 🎁",
        "Día perfecto para actividades al aire libre sin impacto ambiental 🌿",
        "La temperatura ideal para secar ropa al sol y ahorrar energía ☀️",
        "Hoy la naturaleza está en perfecta armonía 🦋",
        "Aprovecha para hacer ejercicio al aire libre 🏃‍♀️",
        "El equilibrio perfecto: ni calefacción ni refrigeración necesaria ⚖️"
    )

    /**
     * 🌡️ Frases para temperatura < 32°C - Calorcito
     */
    private val warmMessages = listOf(
        "Por favor, recuerda mantenerte hidratado 💧",
        "Con este calor es recomendable planes de agua 🏊‍♀️",
        "Aprovecha las sombras naturales que nos regalan los árboles 🌳",
        "Un buen momento para valorar cada gota de agua 💙",
        "Reduce el uso de aire acondicionado y abrete a la ventilación natural 🌬️",
        "Los pueblos costeros tienen la solución natural al calor 🌊",
        "Hidrata tu cuerpo y también tus plantas 🌸"
    )

    /**
     * 🥵 Frases para temperatura < 38°C - Calor
     */
    private val hotMessages = listOf(
        "Busca la sombra y cuida tu piel del sol ☂️",
        "Momento perfecto para apreciar los espacios con aire natural 🌳",
        "El calor nos recuerda la importancia de proteger el clima 🌍",
        "Aprovecha para reflexionar sobre el cambio climático 🤔",
        "Cada acción eco-friendly suma para reducir estas temperaturas 💚",
        "Los árboles son nuestros mejores aliados contra el calor 🌲",
        "Hidratación constante: tu cuerpo y el planeta lo agradecen 💧"
    )

    /**
     * 🔥 Frases para temperatura >= 38°C - Calor extremo
     */
    private val scorchingMessages = listOf(
        "Busca refugio y mantente seguro/a 🏠",
        "Estas temperaturas nos recuerdan que el planeta necesita nuestra ayuda 🆘",
        "Cada grado de más es una llamada de atención de la Tierra 🌍",
        "Es tiempo de actuar por el clima antes de que sea tarde ⏰",
        "El futuro del planeta está en nuestras acciones de hoy 🔮",
        "Protégete y protege el medio ambiente para las próximas generaciones 👨‍👩‍👧‍👦",
        "El calor extremo es un recordatorio de cuidar nuestro hogar común 🏡"
    )

    /**
     * 🎲 Obtiene un mensaje aleatorio según la temperatura
     */
    fun getRandomMessage(temperature: Double): String {
        val messages = when {
            temperature < 0 -> freezingMessages
            temperature < 10 -> coldMessages
            temperature < 18 -> coolMessages
            temperature < 25 -> perfectMessages
            temperature < 32 -> warmMessages
            temperature < 38 -> hotMessages
            else -> scorchingMessages
        }

        return messages[Random.nextInt(messages.size)]
    }

    /**
     * 🌱 Obtiene específicamente un mensaje eco-friendly
     */
    fun getEcoMessage(temperature: Double): String {
        val ecoMessages = when {
            temperature < 0 -> listOf(
                "El frío nos recuerda que la Tierra también necesita descansar 🌍",
                "Aprovecha para reducir tu huella de carbono: menos aire acondicionado 💚",
                "Cada grado importa: apreciemos estos momentos naturales 🌿"
            )
            temperature < 10 -> listOf(
                "Aprovecha para usar ropa abrigada y ahorrar energía en casa ⚡",
                "Un día perfecto para caminar y reducir las emisiones de tu vehículo 🚶‍♀️",
                "La naturaleza se regenera en el frío, igual que tu energía ♻️"
            )
            temperature < 18 -> listOf(
                "Un día ideal para usar bicicleta y cuidar el medio ambiente 🚲",
                "La brisa natural es el mejor aire acondicionado 🍃",
                "Aprovecha para plantar algo verde hoy 🌱"
            )
            temperature < 25 -> listOf(
                "Día perfecto para actividades al aire libre sin impacto ambiental 🌿",
                "La temperatura ideal para secar ropa al sol y ahorrar energía ☀️",
                "El equilibrio perfecto: ni calefacción ni refrigeración necesaria ⚖️"
            )
            temperature < 32 -> listOf(
                "Reduce el uso de aire acondicionado y abrete a la ventilación natural 🌬️",
                "Los pueblos costeros tienen la solución natural al calor 🌊",
                "Hidrata tu cuerpo y también tus plantas 🌸"
            )
            temperature < 38 -> listOf(
                "El calor nos recuerda la importancia de proteger el clima 🌍",
                "Cada acción eco-friendly suma para reducir estas temperaturas 💚",
                "Los árboles son nuestros mejores aliados contra el calor 🌲"
            )
            else -> listOf(
                "Estas temperaturas nos recuerdan que el planeta necesita nuestra ayuda 🆘",
                "Cada grado de más es una llamada de atención de la Tierra 🌍",
                "El futuro del planeta está en nuestras acciones de hoy 🔮"
            )
        }

        return ecoMessages[Random.nextInt(ecoMessages.size)]
    }

    /**
     * 💪 Obtiene específicamente un mensaje motivacional
     */
    fun getMotivationalMessage(temperature: Double): String {
        val motivationalMessages = when {
            temperature < 0 -> listOf(
                "Es día de chocolate calentito ☕",
                "Te recomiendo una buena peli y manta 🛋️",
                "Que el viento helado refresque tus ideas y te inspire a tener un gran día ❄️"
            )
            temperature < 10 -> listOf(
                "Si no tuviéramos invierno, la primavera no resultaría tan agradable 🌸",
                "El frío nos conecta con la naturaleza en su estado más puro 🍃",
                "Cada paso que das hoy cuida el planeta 🌱"
            )
            temperature < 18 -> listOf(
                "Recuerda tus 15 minutos de sol ☀️",
                "Con el buen tiempo aprovecha para pasear y despejarte 🚶",
                "La temperatura perfecta para conectar con la naturaleza 🌳"
            )
            temperature < 25 -> listOf(
                "Temperatura perfecta para dar un buen paseo 🚶‍♂️",
                "El clima ideal existe, y es hoy 🌈",
                "Aprovecha este regalo de la naturaleza 🎁"
            )
            temperature < 32 -> listOf(
                "Por favor, recuerda mantenerte hidratado 💧",
                "Con este calor es recomendable planes de agua 🏊‍♀️",
                "Aprovecha las sombras naturales que nos regalan los árboles 🌳"
            )
            temperature < 38 -> listOf(
                "Busca la sombra y cuida tu piel del sol ☂️",
                "Momento perfecto para apreciar los espacios con aire natural 🌳",
                "Hidratación constante: tu cuerpo y el planeta lo agradecen 💧"
            )
            else -> listOf(
                "Busca refugio y mantente seguro/a 🏠",
                "Es tiempo de actuar por el clima antes de que sea tarde ⏰",
                "Protégete y protege el medio ambiente para las próximas generaciones 👨‍👩‍👧‍👦"
            )
        }

        return motivationalMessages[Random.nextInt(motivationalMessages.size)]
    }

    /**
     * 📊 Obtiene estadísticas de mensajes disponibles
     */
    fun getMessageStats(): String {
        val total = freezingMessages.size + coldMessages.size + coolMessages.size +
                perfectMessages.size + warmMessages.size + hotMessages.size + scorchingMessages.size
        return "Total de $total mensajes eco-motivacionales disponibles 🌱💚"
    }
}