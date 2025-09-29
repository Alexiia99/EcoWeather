package com.lolweather.android.presentation

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import domain.model.LolEmote

/**
 * 🌱 Componente MINI eco-friendly para las tarjetas del carrusel
 */
@Composable
fun MiniEmoteComponent(
    emote: LolEmote,
    temperature: Double,
    modifier: Modifier = Modifier
) {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(emote.name) {
        isVisible = false
        kotlinx.coroutines.delay(50)
        isVisible = true
    }

    val infiniteTransition = rememberInfiniteTransition(label = "mini_emote_animation")

    val floatAnimation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "mini_float"
    )

    AnimatedVisibility(
        visible = isVisible,
        enter = scaleIn(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            )
        ) + fadeIn(animationSpec = tween(200)),
        modifier = modifier
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(60.dp)
                .offset(y = floatAnimation.dp)
        ) {
            // 🎨 Container principal con gradiente eco-friendly
            Box(
                modifier = Modifier
                    .size(55.dp)
                    .clip(CircleShape)
                    .background(getEcoMiniGradient(temperature)),
                contentAlignment = Alignment.Center
            ) {
                // 🌱 Emoji eco-friendly (sin intentar cargar imágenes)
                Text(
                    text = EmoteMapping.getEmoteEmoji(temperature),
                    fontSize = 28.sp
                )
            }
        }
    }
}


fun getEcoMiniGradient(temperature: Double): Brush {
    return when {
        //  Azules naturales para frío
        temperature < 0 -> Brush.radialGradient(
            colors = listOf(
                Color(0xFF81D4FA), // Azul hielo claro
                Color(0xFF0277BD)  // Azul hielo
            )
        )
        //  Grises azulados para frío moderado
        temperature < 10 -> Brush.radialGradient(
            colors = listOf(
                Color(0xFF90A4AE), // Gris azulado
                Color(0xFF455A64)  // Gris oscuro
            )
        )
        //  Verdes bosque para clima fresco
        temperature < 18 -> Brush.radialGradient(
            colors = listOf(
                Color(0xFF81C784), // Verde suave
                Color(0xFF388E3C)  // Verde bosque
            )
        )
        //  Verde vibrante para clima perfecto
        temperature < 25 -> Brush.radialGradient(
            colors = listOf(
                Color(0xFF4CAF50), // Verde perfecto
                Color(0xFF2E7D32)  // Verde intenso
            )
        )
        //  Amarillo sol para clima cálido
        temperature < 32 -> Brush.radialGradient(
            colors = listOf(
                Color(0xFFFFEB3B), // Amarillo brillante
                Color(0xFFF57F17)  // Amarillo dorado
            )
        )
        //  Naranja para calor
        temperature < 38 -> Brush.radialGradient(
            colors = listOf(
                Color(0xFFFF9800), // Naranja sol
                Color(0xFFE65100)  // Naranja intenso
            )
        )
        //  Rojo alerta para calor extremo
        else -> Brush.radialGradient(
            colors = listOf(
                Color(0xFFE57373), // Rojo suave
                Color(0xFFD32F2F)  // Rojo alerta
            )
        )
    }
}

/**
 * 🎯 Componente MICRO eco-friendly para listas súper compactas
 */
@Composable
fun MicroEmoteComponent(
    emote: LolEmote,
    temperature: Double,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(getEcoMiniGradient(temperature))
    ) {

        Text(
            text = EmoteMapping.getEmoteEmoji(temperature),
            fontSize = 20.sp
        )
    }
}

/**
 *  Componente de emote con animación de aparición en cadena
 */
@Composable
fun DelayedMiniEmoteComponent(
    emote: LolEmote,
    temperature: Double,
    delayMillis: Int = 0,
    modifier: Modifier = Modifier
) {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(emote.name) {
        kotlinx.coroutines.delay(delayMillis.toLong())
        isVisible = true
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(
            initialOffsetY = { it },
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            )
        ) + fadeIn(animationSpec = tween(400)),
        modifier = modifier
    ) {
        MiniEmoteComponent(
            emote = emote,
            temperature = temperature
        )
    }
}

/**
 *  Componente especial para el emote del día más destacado
 */
@Composable
fun HighlightedMiniEmoteComponent(
    emote: LolEmote,
    temperature: Double,
    isHighlighted: Boolean = false,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "highlighted_emote")

    // Animación especial para elementos destacados
    val highlightAnimation by infiniteTransition.animateFloat(
        initialValue = if (isHighlighted) 1f else 1f,
        targetValue = if (isHighlighted) 1.1f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "highlight"
    )

    Box(
        modifier = modifier
            .scale(highlightAnimation)
            .offset(y = if (isHighlighted) (-2).dp else 0.dp),
        contentAlignment = Alignment.Center
    ) {
        MiniEmoteComponent(
            emote = emote,
            temperature = temperature
        )

        if (isHighlighted) {
            Box(
                modifier = Modifier
                    .size(85.dp)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                EmoteMapping.getGifThemeColor(
                                    EmoteMapping.getAnimatedGifName(temperature)
                                ).copy(alpha = 0.3f),
                                Color.Transparent
                            )
                        )
                    )
            )
        }
    }
}