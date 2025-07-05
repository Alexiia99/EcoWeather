package com.lolweather.android.presentation

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import domain.model.WeatherInfo
import org.koin.androidx.compose.koinViewModel

/**
 * 🎮 Pantalla principal de LoL Weather
 * ¡Diseño mejorado con geolocalización automática!
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // 📍 Launcher para solicitar permisos de ubicación
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions.values.any { it }
        if (granted) {
            // Si se concedieron permisos, actualizar con ubicación
            viewModel.refreshWithLocation()
        } else {
            // Si no se concedieron, usar Valencia como fallback
            viewModel.getWeatherByCity("Valencia")
        }
    }

    // 🚀 Solicitar permisos al iniciar si no los tenemos
    LaunchedEffect(Unit) {
        if (!viewModel.hasLocationPermission()) {
            locationPermissionLauncher.launch(viewModel.getRequiredPermissions())
        }
    }

    // 🎨 Fondo con gradiente que cambia según la temperatura
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(getGradientForTemperature(uiState.weatherInfo?.temperature))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 🔝 Barra superior
            TopAppBar(
                title = {
                    Text(
                        "LoL Weather",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 24.sp
                    )
                },
                actions = {
                    IconButton(onClick = {
                        // 📍 Refresh usando ubicación automáticamente
                        viewModel.refresh()
                    }) {
                        Icon(
                            Icons.Default.Refresh,
                            contentDescription = "Actualizar ubicación",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            // 📱 Contenido principal
            val currentState = uiState
            when {
                currentState.isLoading -> {
                    LoadingContent()
                }

                currentState.error != null -> {
                    ErrorContent(
                        error = currentState.error,
                        onRetry = { viewModel.refresh() }
                    )
                }

                currentState.weatherInfo != null -> {
                    WeatherContent(weatherInfo = currentState.weatherInfo)
                }
            }
        }
    }
}

/**
 * 🌤️ Contenido principal del clima - DISEÑO MEJORADO
 */
@Composable
fun WeatherContent(weatherInfo: WeatherInfo) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        // 🏙️ Nombre de la ciudad con estilo mejorado
        Text(
            text = weatherInfo.cityName,
            fontSize = 32.sp,
            fontWeight = FontWeight.Light,
            color = Color.White,
            textAlign = TextAlign.Center,
            letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        // ✨ Pequeño indicador de ubicación
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.7f),
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Tu ubicación",
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.7f),
                fontWeight = FontWeight.Light
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 🎮 EMOTE SIN TEXTO - Solo la imagen épica
        ImprovedEmoteComponent(
            emote = weatherInfo.lolEmote,
            temperature = weatherInfo.temperature
        )

        Spacer(modifier = Modifier.height(32.dp))

        // 🌡️ Temperatura con diseño más impactante
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "${weatherInfo.temperature.toInt()}",
                fontSize = 108.sp,
                fontWeight = FontWeight.Thin,
                color = Color.White,
                letterSpacing = (-4).sp
            )
            Text(
                text = "°",
                fontSize = 64.sp,
                fontWeight = FontWeight.Thin,
                color = Color.White.copy(alpha = 0.8f),
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 🎮 Descripción del emote más sutil
        Text(
            text = weatherInfo.lolEmote.description,
            fontSize = 18.sp,
            color = Color.White.copy(alpha = 0.9f),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.5.sp
        )

        Spacer(modifier = Modifier.height(40.dp))

        // 📊 Card con detalles mejorado
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White.copy(alpha = 0.12f)
            ),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                WeatherDetailRow(
                    label = "Sensación",
                    value = "${weatherInfo.feelsLike.toInt()}°",
                    icon = "🌡️"
                )

                Spacer(modifier = Modifier.height(12.dp))

                WeatherDetailRow(
                    label = "Humedad",
                    value = "${weatherInfo.humidity}%",
                    icon = "💧"
                )

                Spacer(modifier = Modifier.height(12.dp))

                WeatherDetailRow(
                    label = "Viento",
                    value = "${weatherInfo.windSpeed.toInt()} km/h",
                    icon = "💨"
                )

                Spacer(modifier = Modifier.height(12.dp))

                WeatherDetailRow(
                    label = "Condición",
                    value = weatherInfo.description.replaceFirstChar { it.uppercase() },
                    icon = "☀️"
                )
            }
        }
    }
}

/**
 * 🔄 Contenido de carga mejorado
 */
@Composable
fun LoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                color = Color.White,
                strokeWidth = 4.dp,
                modifier = Modifier.size(64.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "📍 Obteniendo tu ubicación...",
                color = Color.White,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Preparando el clima mágico",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * ❌ Contenido de error
 */
@Composable
fun ErrorContent(error: String, onRetry: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "😵",
            fontSize = 64.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "¡Oops! Algo salió mal",
            fontSize = 20.sp,
            color = Color.White,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = error,
            fontSize = 14.sp,
            color = Color.White.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White.copy(alpha = 0.2f)
            )
        ) {
            Text("Reintentar", color = Color.White)
        }
    }
}

/**
 * 📋 Fila de detalle mejorada con iconos
 */
@Composable
fun WeatherDetailRow(
    label: String,
    value: String,
    icon: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = icon,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = label,
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal
            )
        }

        Text(
            text = value,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

/**
 * 🎨 Obtiene el gradiente según la temperatura
 * Cada rango tiene colores únicos inspirados en LoL
 */
@Composable
fun getGradientForTemperature(temperature: Double?): Brush {
    return when {
        temperature == null -> Brush.verticalGradient(
            colors = listOf(Color(0xFF2196F3), Color(0xFF21CBF3))
        )
        // ❄️ Congelándote (bajo 0°)
        temperature < 0 -> Brush.verticalGradient(
            colors = listOf(Color(0xFF0D47A1), Color(0xFF1976D2))
        )
        // 🧊 Frío (0-10°)
        temperature < 10 -> Brush.verticalGradient(
            colors = listOf(Color(0xFF1565C0), Color(0xFF42A5F5))
        )
        // 😊 Fresco (10-18°)
        temperature < 18 -> Brush.verticalGradient(
            colors = listOf(Color(0xFF0277BD), Color(0xFF29B6F6))
        )
        // 😎 Perfecto (18-25°)
        temperature < 25 -> Brush.verticalGradient(
            colors = listOf(Color(0xFF2E7D32), Color(0xFF66BB6A))
        )
        // 😅 Calorcito (25-32°)
        temperature < 32 -> Brush.verticalGradient(
            colors = listOf(Color(0xFFF57F17), Color(0xFFFFCA28))
        )
        // 🥵 Calor (32-38°)
        temperature < 38 -> Brush.verticalGradient(
            colors = listOf(Color(0xFFE65100), Color(0xFFFF9800))
        )
        // 🔥 ¡Te derrites! (38°+)
        else -> Brush.verticalGradient(
            colors = listOf(Color(0xFFD84315), Color(0xFFFF5722))
        )
    }
}