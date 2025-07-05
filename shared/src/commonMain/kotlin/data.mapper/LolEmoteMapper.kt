package data.mapper

import domain.model.*

class LolEmoteMapper {

    private val emotes = listOf(
        // Frío extremo (bajo 0°) - ¡Congelándote!
        LolEmote(
            name = "Braum Tiritando",
            description = "¡Hace mucho frío, amigo!",
            imagePath = "emotes/braum_cold.png",
            temperatureRange = TemperatureRange.FreezingCold
        ),
        LolEmote(
            name = "Poro Congelado",
            description = "Brrr... necesito una bufanda",
            imagePath = "emotes/poro_frozen.png",
            temperatureRange = TemperatureRange.FreezingCold
        ),
        LolEmote(
            name = "Anivia Helada",
            description = "Incluso yo tengo frío",
            imagePath = "emotes/anivia_frozen.png",
            temperatureRange = TemperatureRange.FreezingCold
        ),

        // Frío (0-10°) - Fresquito pero ok
        LolEmote(
            name = "Ashe Preparada",
            description = "Fresco, pero controlable",
            imagePath = "emotes/ashe_cool.png",
            temperatureRange = TemperatureRange.Cold
        ),
        LolEmote(
            name = "Sejuani Montando",
            description = "Perfecto para cabalgar",
            imagePath = "emotes/sejuani_riding.png",
            temperatureRange = TemperatureRange.Cold
        ),

        // Fresco (10-18°) - Muy agradable
        LolEmote(
            name = "Garen Aprobando",
            description = "Día perfecto para entrenar",
            imagePath = "emotes/garen_thumbsup.png",
            temperatureRange = TemperatureRange.Cool
        ),
        LolEmote(
            name = "Lux Brillante",
            description = "¡Qué día tan bonito!",
            imagePath = "emotes/lux_happy.png",
            temperatureRange = TemperatureRange.Cool
        ),

        // Cómodo (18-25°) - ¡PERFECTO!
        LolEmote(
            name = "Thumbs Up Clásico",
            description = "¡Temperatura perfecta!",
            imagePath = "emotes/classic_thumbsup.png",
            temperatureRange = TemperatureRange.Comfortable
        ),
        LolEmote(
            name = "Ezreal Confiado",
            description = "Día ideal para explorar",
            imagePath = "emotes/ezreal_confident.png",
            temperatureRange = TemperatureRange.Comfortable
        ),
        LolEmote(
            name = "Ahri Sonriendo",
            description = "Me encanta este clima",
            imagePath = "emotes/ahri_smile.png",
            temperatureRange = TemperatureRange.Comfortable
        ),
        LolEmote(
            name = "Yasuo Zen",
            description = "En perfecta armonía",
            imagePath = "emotes/yasuo_zen.png",
            temperatureRange = TemperatureRange.Comfortable
        ),

        // Calorcito (25-32°) - Empezando a sudar
        LolEmote(
            name = "Jinx Sudando",
            description = "Empiezo a sudar un poquito...",
            imagePath = "emotes/jinx_sweating.png",
            temperatureRange = TemperatureRange.Warm
        ),
        LolEmote(
            name = "Vi Quitándose Guantes",
            description = "Hace calorcito, ¿eh?",
            imagePath = "emotes/vi_warm.png",
            temperatureRange = TemperatureRange.Warm
        ),

        // Calor (32-38°) - ¡Qué calor!
        LolEmote(
            name = "Teemo Agobiado",
            description = "¡Demasiado calor para mí!",
            imagePath = "emotes/teemo_hot.png",
            temperatureRange = TemperatureRange.Hot
        ),
        LolEmote(
            name = "Ziggs Explosivo",
            description = "¡Esto está que arde!",
            imagePath = "emotes/ziggs_hot.png",
            temperatureRange = TemperatureRange.Hot
        ),
        LolEmote(
            name = "Graves Sudoroso",
            description = "Necesito una cerveza fría",
            imagePath = "emotes/graves_sweating.png",
            temperatureRange = TemperatureRange.Hot
        ),

        // Calor extremo (38°+) - ¡ME DERRITO!
        LolEmote(
            name = "Brand Derritiéndose",
            description = "Incluso yo me derrito",
            imagePath = "emotes/brand_melting.png",
            temperatureRange = TemperatureRange.Scorching
        ),
        LolEmote(
            name = "Auxilio Total",
            description = "¡AUXILIO, ME ASO!",
            imagePath = "emotes/help_melting.png",
            temperatureRange = TemperatureRange.Scorching
        ),
        LolEmote(
            name = "Azir Desesperado",
            description = "¡Y yo que pensaba que el desierto era caliente!",
            imagePath = "emotes/azir_desperate.png",
            temperatureRange = TemperatureRange.Scorching
        ),
        LolEmote(
            name = "Annie Sofocada",
            description = "Ni mis llamas son tan intensas",
            imagePath = "emotes/annie_overwhelmed.png",
            temperatureRange = TemperatureRange.Scorching
        )
    )

    fun getEmoteForTemperature(temperature: Double): LolEmote {
        val matchingEmotes = emotes.filter { emote ->
            temperature >= emote.temperatureRange.min && temperature < emote.temperatureRange.max
        }

        return matchingEmotes.randomOrNull()
            ?: emotes.first { it.temperatureRange is TemperatureRange.Comfortable }
    }

    fun getAllEmotes(): List<LolEmote> = emotes

    fun getEmotesByRange(temperatureRange: TemperatureRange): List<LolEmote> {
        return emotes.filter { it.temperatureRange::class == temperatureRange::class }
    }
}