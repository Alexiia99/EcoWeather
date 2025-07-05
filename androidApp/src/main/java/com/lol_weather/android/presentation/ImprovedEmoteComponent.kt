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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import domain.model.LolEmote

/**
 * 🔍 Helper función para verificar si existe un recurso drawable
 */
@Composable
fun rememberDrawableResourceId(resourceName: String): Int {
    val context = LocalContext.current
    return remember(resourceName) {
        context.resources.getIdentifier(
            resourceName,
            "drawable",
            context.packageName
        )
    }
}

/**
 * 🎮 Componente de emote MEJORADO - SIN texto, solo imagen épica
 */
@Composable
fun ImprovedEmoteComponent(
    emote: LolEmote,
    temperature: Double,
    modifier: Modifier = Modifier
) {
    // ✨ Animación de entrada más suave
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(emote.name) {
        isVisible = false
        kotlinx.coroutines.delay(150)
        isVisible = true
    }

    // 🎭 Animaciones más sutiles y elegantes
    val infiniteTransition = rememberInfiniteTransition(label = "emote_animation")

    // Flotación muy sutil
    val floatAnimation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 6f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float"
    )

    // Brillo sutil para temperaturas extremas
    val glowAnimation by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    // Rotación muy sutil para elementos fríos
    val rotationAnimation by infiniteTransition.animateFloat(
        initialValue = -2f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "rotation"
    )

    AnimatedVisibility(
        visible = isVisible,
        enter = scaleIn(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMediumLow
            )
        ) + fadeIn(animationSpec = tween(500)),
        modifier = modifier
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(200.dp)
                .offset(y = floatAnimation.dp)
        ) {
            // 🌟 Efecto de sombra/halo exterior
            Box(
                modifier = Modifier
                    .size(190.dp)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.15f),
                                Color.Transparent
                            ),
                            radius = 200f
                        )
                    )
            )

            // 🎨 Container principal con gradiente mejorado
            Box(
                modifier = Modifier
                    .size(170.dp)
                    .shadow(
                        elevation = 12.dp,
                        shape = CircleShape,
                        ambientColor = Color.White.copy(alpha = 0.1f)
                    )
                    .clip(CircleShape)
                    .background(getEnhancedGradient(temperature)),
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
                        contentDescription = null, // Sin descripción para mejor UX
                        modifier = Modifier
                            .size(140.dp)
                            .scale(
                                when {
                                    temperature > 35 -> glowAnimation // Efecto de calor
                                    temperature < 5 -> 1f // Sin efectos para frío
                                    else -> 1f
                                }
                            ),
                        contentScale = ContentScale.Fit
                    )
                } else {
                    // 🎭 Emoji de fallback más grande y limpio
                    Text(
                        text = EmoteMapping.getEmoteEmoji(temperature),
                        fontSize = 84.sp,
                        modifier = Modifier.scale(
                            if (temperature > 35) glowAnimation else 1f
                        )
                    )
                }
            }
        }
    }
}

/**
 * 🌈 Gradientes mejorados con más profundidad
 */
fun getEnhancedGradient(temperature: Double): Brush {
    return when {
        temperature < 0 -> Brush.radialGradient(
            colors = listOf(
                Color(0xFF64B5F6),
                Color(0xFF1565C0),
                Color(0xFF0D47A1)
            )
        )
        temperature < 10 -> Brush.radialGradient(
            colors = listOf(
                Color(0xFF81C784),
                Color(0xFF42A5F5),
                Color(0xFF1976D2)
            )
        )
        temperature < 18 -> Brush.radialGradient(
            colors = listOf(
                Color(0xFFAED581),
                Color(0xFF29B6F6),
                Color(0xFF0277BD)
            )
        )
        temperature < 25 -> Brush.radialGradient(
            colors = listOf(
                Color(0xFFA5D6A7),
                Color(0xFF66BB6A),
                Color(0xFF2E7D32)
            )
        )
        temperature < 32 -> Brush.radialGradient(
            colors = listOf(
                Color(0xFFFFD54F),
                Color(0xFFFFCA28),
                Color(0xFFF57F17)
            )
        )
        temperature < 38 -> Brush.radialGradient(
            colors = listOf(
                Color(0xFFFFAB40),
                Color(0xFFFF9800),
                Color(0xFFE65100)
            )
        )
        else -> Brush.radialGradient(
            colors = listOf(
                Color(0xFFFF7043),
                Color(0xFFE53935),
                Color(0xFFB71C1C)
            )
        )
    }
}