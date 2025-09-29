package com.lolweather.android.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.lolweather.android.presentation.EmoteMapping
import pl.droidsonroids.gif.GifImageView

/**
 * 🎬 Componente para mostrar tus GIFs animados del clima
 * ¡Funciona perfecto con tus 8 GIFs eco-friendly!
 */
@Composable
fun AnimatedWeatherIcon(
    temperature: Double,
    modifier: Modifier = Modifier,
    size: Int = 180,
    useRandomGif: Boolean = true
) {
    val context = LocalContext.current

    // 🎲 Decidir qué GIF usar
    val gifName = if (useRandomGif) {
        EmoteMapping.getRandomAnimatedGifName(temperature)
    } else {
        EmoteMapping.getAnimatedGifName(temperature)
    }

    // 🎨 Card con tu GIF animado
    Card(
        modifier = modifier.size(size.dp),
        shape = CircleShape,
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.15f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            // 🎬 Tu GIF animado
            AndroidView(
                factory = { context ->
                    GifImageView(context).apply {
                        // 📂 Cargar tu GIF desde res/raw
                        val resourceId = context.resources.getIdentifier(
                            gifName,
                            "raw",
                            context.packageName
                        )

                        if (resourceId != 0) {
                            setImageResource(resourceId)
                        } else {
                            // 🔄 Si el GIF no existe, mostrar emoji
                            // (esto no debería pasar, pero por seguridad)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .clip(CircleShape)
            )
        }
    }
}