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
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import domain.model.WeatherInfo
import org.koin.androidx.compose.koinViewModel

/**
 * 🎮 Pantalla principal de LoL Weather
 * ¡ACTUALIZADA con búsqueda de ciudades!
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(
    selectedCity: String? = null, // 🔍 NUEVA: ciudad seleccionada desde búsqueda
    onNavigateToForecast: () -> Unit = {},
    onNavigateToSearch: () -> Unit = {}, // 🔍 NUEVA: navegación a búsqueda
    viewModel: WeatherViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // 🔍 NUEVA: Efecto para cargar ciudad seleccionada
    LaunchedEffect(selectedCity) {
        selectedCity?.let { cityName ->
            viewModel.getWeatherByCity(cityName)
        }
    }

    // 📍 Launcher para solicitar permisos de ubicación
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions.values.any { it }
        if (granted) {
            viewModel.refreshWithLocation()
        } else {
            viewModel.getWeatherByCity("Valencia")
        }
    }

    // 🚀 Solicitar permisos al iniciar si no los tenemos y no hay ciudad seleccionada
    LaunchedEffect(Unit) {
        if (selectedCity == null && !viewModel.hasLocationPermission()) {
            locationPermissionLauncher.launch(viewModel.getRequiredPermissions())
        }
    }

    // 🎨 Fondo con gradiente que cambia según la temperatura
    val backgroundGradient = remember(uiState.weatherInfo?.temperature) {
        WeatherUtils.getGradientForTemperature(uiState.weatherInfo?.temperature)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 🔝 Barra superior ACTUALIZADA con botón de búsqueda
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
                    // 🔍 ¡NUEVO BOTÓN DE BÚSQUEDA ÉPICO!
                    IconButton(onClick = onNavigateToSearch) {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "Buscar ciudad",
                            tint = Color.White
                        )
                    }

                    // 📅 Botón de pronóstico
                    IconButton(onClick = onNavigateToForecast) {
                        Icon(
                            Icons.Default.DateRange,
                            contentDescription = "Ver pronóstico 5 días",
                            tint = Color.White
                        )
                    }

                    // 📍 Botón de refresh
                    IconButton(onClick = {
                        if (selectedCity != null) {
                            // Si hay ciudad seleccionada, refrescar esa ciudad
                            viewModel.getWeatherByCity(selectedCity)
                        } else {
                            // Si no, usar ubicación
                            viewModel.refresh()
                        }
                    }) {
                        Icon(
                            Icons.Default.Refresh,
                            contentDescription = "Actualizar",
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
            when {
                uiState.isLoading -> {
                    LoadingContent()
                }

                uiState.error != null -> {
                    ErrorContent(
                        error = uiState.error!!,
                        onRetry = {
                            if (selectedCity != null) {
                                viewModel.getWeatherByCity(selectedCity)
                            } else {
                                viewModel.refresh()
                            }
                        }
                    )
                }

                uiState.weatherInfo != null -> {
                    WeatherContent(
                        weatherInfo = uiState.weatherInfo!!,
                        isCustomCity = selectedCity != null, // 🔍 NUEVA: indicador de ciudad personalizada
                        onViewForecast = onNavigateToForecast,
                        onSearchCity = onNavigateToSearch // 🔍 NUEVA: búsqueda desde contenido
                    )
                }
            }
        }
    }
}

/**
 * 🌤️ Contenido principal del clima - ACTUALIZADO con indicadores de ciudad
 */
@Composable
fun WeatherContent(
    weatherInfo: WeatherInfo,
    isCustomCity: Boolean = false, // 🔍 NUEVA: indica si es ciudad buscada
    onViewForecast: () -> Unit = {},
    onSearchCity: () -> Unit = {} // 🔍 NUEVA: función de búsqueda
) {
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

        // ✨ Indicador de ubicación ACTUALIZADO
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
                text = if (isCustomCity) "Ciudad seleccionada" else "Tu ubicación",
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

        Spacer(modifier = Modifier.height(32.dp))

        // 🔍 NUEVA: Botón de búsqueda de ciudades si no es personalizada
        if (!isCustomCity) {
            Button(
                onClick = onSearchCity,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White.copy(alpha = 0.15f),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(28.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "🔍 Buscar otra ciudad",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // 📅 Botón de pronóstico
        Button(
            onClick = onViewForecast,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 20.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White.copy(alpha = 0.2f),
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(28.dp)
        ) {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "📅 Ver Pronóstico 5 Días",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

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
                    value = WeatherUtils.formatTemperature(weatherInfo.feelsLike),
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