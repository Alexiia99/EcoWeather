package com.lolweather.android.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * 🎨 Colores inspirados en League of Legends
 * Azules y dorados que combinan con los emotes
 */
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF1E88E5),        // Azul LoL
    secondary = Color(0xFFC89B3C),      // Dorado LoL
    tertiary = Color(0xFF3700B3),       // Púrpura mágico
    background = Color(0xFF0F1419),     // Azul muy oscuro
    surface = Color(0xFF1E2328),        // Gris azulado
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onTertiary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,
)

/**
 * 🎮 Tema principal de LoL Weather
 * Diseñado para que los emotes de LoL destaquen
 */
@Composable
fun LolWeatherTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = MaterialTheme.typography, // Usamos la tipografía por defecto
        content = content
    )
}