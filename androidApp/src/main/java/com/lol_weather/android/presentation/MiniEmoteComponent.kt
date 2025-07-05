package com.lolweather.android.presentation

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import domain.model.LolEmote

/**
 * 🎮 Componente MINI de emote para las tarjetas del carrusel
 * ¡Versión compacta y optimizada!
 */
@Composable
fun MiniEmoteComponent(
    emote: LolEmote,
    temperature: Double,
    modifier: Modifier = Modifier
) {
    // ✨ Animación de entrada suave pero rápida
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(emote.name) {
        isVisible = false
        kotlinx.coroutines.delay(50) // Más rápida
        isVisible = true
    }

    // 🎭 Animaciones muy sutiles para no distraer
    val infiniteTransition = rememberInfiniteTransition(label = "mini_emote_animation")

    // Flotación muy sutil
    val floatAnimation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2f, // Movimiento mínimo
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
                .size(60.dp) // Tamaño fijo más pequeño
                .offset(y = floatAnimation.dp)
        ) {
            // 🎨 Container principal simplificado
            Box(
                modifier = Modifier
                    .size(55.dp)
                    .clip(CircleShape)
                    .background(getMiniGradient(temperature)),
                contentAlignment = Alignment.Center
            ) {
                // 🖼️ Imagen del emote o emoji de fallback
                val imageName = EmoteMapping.getImageResourceName(temperature)
                val resourceId = if (imageName != null) {
                    rememberDrawableResourceId(imageName)
                } else {
                    0
                }

                if (resourceId != 0) {
                    // ✅ Mostrar imagen real
                    Image(
                        painter = painterResource(id = resourceId),
                        contentDescription = null,
                        modifier = Modifier.size(45.dp), // Imagen más pequeña
                        contentScale = ContentScale.Fit
                    )
                } else {
                    // 🎭 Emoji de fallback para versión mini
                    Text(
                        text = EmoteMapping.getEmoteEmoji(temperature),
                        fontSize = 28.sp // Emoji más pequeño
                    )
                }
            }
        }
    }
}

/**
 * 🌈 Gradientes optimizados para la versión mini
 * Menos colores para mejor rendimiento
 */
fun getMiniGradient(temperature: Double): Brush {
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

/**
 * 🎯 Componente MICRO de emote para uso en listas muy compactas
 * ¡Aún más pequeño para casos especiales!
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
            .background(getMiniGradient(temperature))
    ) {
        // Solo emoji para la versión micro
        Text(
            text = EmoteMapping.getEmoteEmoji(temperature),
            fontSize = 20.sp
        )
    }
}

/**
 * 🎮 Componente de emote con animación de aparición en cadena
 * ¡Perfecto para listas que aparecen secuencialmente!
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
 * 🎪 Componente especial para el emote del día más destacado
 * ¡Con efectos extra para el día actual!
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

    // Rotación sutil para elementos destacados
    val rotationAnimation by infiniteTransition.animateFloat(
        initialValue = if (isHighlighted) -5f else 0f,
        targetValue = if (isHighlighted) 5f else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "rotation"
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

        // ✨ Brillo extra para elementos destacados
        if (isHighlighted) {
            Box(
                modifier = Modifier
                    .size(85.dp)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color.Yellow.copy(alpha = 0.2f),
                                Color.Transparent
                            )
                        )
                    )
            )
        }
    }
}