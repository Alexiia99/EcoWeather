package com.lolweather.android.presentation

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import domain.model.DayForecast
import domain.model.WeekForecast
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForecastScreen(
    selectedCity: String? = null,
    onBackClick: () -> Unit,
    viewModel: ForecastViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()


    LaunchedEffect(selectedCity) {
        selectedCity?.let { cityName ->
            viewModel.getCompleteWeatherByCity(cityName)
        }
    }


    val backgroundGradient = remember(uiState.weekForecast) {
        getWeekGradient(uiState.weekForecast)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            TopAppBar(
                title = {
                    Column {
                        Text(
                            "Pronóstico 5 Días",
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 22.sp
                        )
                        uiState.weekForecast?.let { forecast ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    "${forecast.cityName}, ${forecast.country}",
                                    color = Color.White.copy(alpha = 0.8f),
                                    fontSize = 14.sp
                                )


                                if (selectedCity != null) {
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        "🔍",
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.height(16.dp))


            when {
                uiState.isLoading -> {
                    ForecastLoadingContent()
                }
                uiState.error != null -> {
                    ForecastErrorContent(
                        error = uiState.error!!,
                        onRetry = { viewModel.refresh() }
                    )
                }
                uiState.weekForecast != null -> {
                    ForecastMainContent(
                        weekForecast = uiState.weekForecast!!
                    )
                }
            }
        }
    }
}

/**
 *  CONTENIDO PRINCIPAL DEL PRONÓSTICO
 */
@Composable
fun ForecastMainContent(weekForecast: WeekForecast) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        //  GRÁFICO DE TEMPERATURAS
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White.copy(alpha = 0.15f)
            ),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    "📈 Temperaturas de la Semana",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(12.dp))

                // 🎯 Gráfico simple pero efectivo
                TemperatureChart(
                    days = weekForecast.days,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        //  CARRUSEL HORIZONTAL DE DÍAS
        Text(
            "🎮 Emotes de LoL por Día",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp), // Reducido de 16dp
            contentPadding = PaddingValues(horizontal = 4.dp), // Reducido de 8dp
            modifier = Modifier.fillMaxWidth()
        ) {
            itemsIndexed(weekForecast.days) { index, day ->
                DayForecastCard(
                    dayForecast = day,
                    isToday = index == 0
                    // SIN animateItemPlacement() para evitar errores
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 📊 RESUMEN DE LA SEMANA
        WeekSummaryCard(weekForecast = weekForecast)
    }
}

/**
 *  TARJETA DE DÍA REDISEÑADA
 */
@Composable
fun DayForecastCard(
    dayForecast: DayForecast,
    isToday: Boolean,
    modifier: Modifier = Modifier
) {
    val cardColor = if (isToday) {
        Color.White.copy(alpha = 0.25f)
    } else {
        Color.White.copy(alpha = 0.15f)
    }

    Card(
        modifier = modifier
            .width(140.dp)
            .height(200.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isToday) 6.dp else 3.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            //  SECCIÓN FECHA
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (isToday) "HOY" else dayForecast.dayName,
                    color = if (isToday) Color(0xFFFFD700) else Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )

                if (!isToday && dayForecast.dayName != "Mañana") {
                    Text(
                        text = dayForecast.shortDate,
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 10.sp
                    )
                }
            }


            //  TEMPERATURAS
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Máxima
                Text(
                    text = WeatherUtils.formatTemperature(dayForecast.maxTemp),
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                // Mínima con separador visual
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .width(20.dp)
                            .height(1.dp)
                            .background(Color.White.copy(alpha = 0.4f))
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = WeatherUtils.formatTemperature(dayForecast.minTemp),
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // 📝 DESCRIPCIÓN - Una sola línea
            Text(
                text = dayForecast.description.take(15), // Máximo 15 caracteres
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 9.sp,
                textAlign = TextAlign.Center,
                maxLines = 1,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

/**
 * 📊 GRÁFICO DE TEMPERATURAS - VERSIÓN ORIGINAL RESTAURADA
 */
@Composable
fun TemperatureChart(
    days: List<DayForecast>,
    modifier: Modifier = Modifier
) {
    if (days.isEmpty()) return

    val maxTemp = days.maxOfOrNull { it.maxTemp } ?: 30.0
    val minTemp = days.minOfOrNull { it.minTemp } ?: 10.0

    Box(modifier = modifier) {
        // 📈 Líneas de fondo
        repeat(4) { i ->
            val yPosition = (i + 1) * 0.2f
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .align(Alignment.BottomStart)
                    .offset(y = (-yPosition * 120).dp)
                    .background(Color.White.copy(alpha = 0.2f))
            )
        }

        // 📊 Barras de temperatura para cada día - DISEÑO ORIGINAL
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            days.forEachIndexed { index, day ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxHeight()
                ) {
                    Spacer(modifier = Modifier.weight(1f))

                    // 🌡️ Temperatura máxima
                    Text(
                        text = WeatherUtils.formatTemperature(day.maxTemp),
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // 📊 Barra visual
                    val normalizedHeight = WeatherUtils.normalizeTemperatureHeight(
                        temperature = day.maxTemp,
                        minTemp = minTemp,
                        maxTemp = maxTemp
                    )

                    Box(
                        modifier = Modifier
                            .width(20.dp)
                            .fillMaxHeight(normalizedHeight.toFloat())
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        WeatherUtils.getTemperatureColor(day.maxTemp),
                                        WeatherUtils.getTemperatureColor(day.minTemp)
                                    )
                                )
                            )
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // 🌡️ Temperatura mínima
                    Text(
                        text = WeatherUtils.formatTemperature(day.minTemp),
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 10.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // 📅 Día
                    Text(
                        text = day.dayName.take(3),
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 10.sp
                    )
                }
            }
        }
    }
}

/**
 * 📋 RESUMEN DE LA SEMANA
 */
@Composable
fun WeekSummaryCard(weekForecast: WeekForecast) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.15f)
        ),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                "📊 Resumen de la Semana",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SummaryItem(
                    label = "Máxima",
                    value = WeatherUtils.formatTemperature(weekForecast.maxWeekTemp),
                    icon = "🔥"
                )
                SummaryItem(
                    label = "Mínima",
                    value = WeatherUtils.formatTemperature(weekForecast.minWeekTemp),
                    icon = "❄️"
                )
                SummaryItem(
                    label = "Promedio",
                    value = WeatherUtils.formatTemperature((weekForecast.maxWeekTemp + weekForecast.minWeekTemp) / 2),
                    icon = "📊"
                )
            }
        }
    }
}

@Composable
fun SummaryItem(label: String, value: String, icon: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = icon, fontSize = 24.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 12.sp
        )
    }
}

/**
 * 🎨 Función auxiliar para gradiente de la semana (NO @Composable)
 */
fun getWeekGradient(weekForecast: WeekForecast?): Brush {
    return if (weekForecast != null) {
        WeatherUtils.getWeekGradient(weekForecast.maxWeekTemp, weekForecast.minWeekTemp)
    } else {
        WeatherUtils.getGradientForTemperature(null)
    }
}

/**
 * 🔄 Contenido de carga
 */
@Composable
fun ForecastLoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(
                color = Color.White,
                strokeWidth = 4.dp,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "📅 Cargando pronóstico épico...",
                color = Color.White,
                fontSize = 16.sp
            )
        }
    }
}

/**
 * ❌ Contenido de error
 */
@Composable
fun ForecastErrorContent(error: String, onRetry: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("😵", fontSize = 64.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "Error al cargar pronóstico",
            fontSize = 20.sp,
            color = Color.White,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            error,
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