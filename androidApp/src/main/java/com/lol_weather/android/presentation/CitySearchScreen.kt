package com.lolweather.android.presentation

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.androidx.compose.koinViewModel

/**
 * 🔍 PANTALLA DE BÚSQUEDA DE CIUDADES
 * ¡Busca cualquier ciudad del mundo!
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun CitySearchScreen(
    onBackClick: () -> Unit,
    onCitySelected: (String) -> Unit,
    viewModel: CitySearchViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    // 🎨 Fondo con gradiente
    val backgroundGradient = WeatherUtils.getGradientForTemperature(22.0) // Gradiente neutral

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
            // 🔝 Barra superior
            TopAppBar(
                title = {
                    Text(
                        "Buscar Ciudad",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 22.sp
                    )
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

            // 🔍 CAMPO DE BÚSQUEDA ÉPICO
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.15f)
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                        viewModel.searchCities(it)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    placeholder = {
                        Text(
                            "Buscar ciudad... (ej: Madrid, Tokyo)",
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "Buscar",
                            tint = Color.White.copy(alpha = 0.8f)
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(
                                onClick = {
                                    searchQuery = ""
                                    viewModel.clearSearch()
                                }
                            ) {
                                Icon(
                                    Icons.Default.Clear,
                                    contentDescription = "Limpiar",
                                    tint = Color.White.copy(alpha = 0.8f)
                                )
                            }
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Color.White.copy(alpha = 0.5f),
                        unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                        cursorColor = Color.White
                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            if (searchQuery.isNotEmpty()) {
                                keyboardController?.hide()
                                onCitySelected(searchQuery)
                            }
                        }
                    )
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 📱 CONTENIDO PRINCIPAL
            when {
                searchQuery.isEmpty() -> {
                    // 🌟 CIUDADES FAVORITAS / POPULARES
                    PopularCitiesContent(
                        cities = uiState.popularCities,
                        onCityClick = onCitySelected,
                        onAddToFavorites = { viewModel.addToFavorites(it) }
                    )
                }

                uiState.isSearching -> {
                    // 🔄 BUSCANDO...
                    SearchingContent()
                }

                uiState.searchResults.isNotEmpty() -> {
                    // 📋 RESULTADOS DE BÚSQUEDA
                    SearchResultsContent(
                        results = uiState.searchResults,
                        onCityClick = onCitySelected,
                        onAddToFavorites = { viewModel.addToFavorites(it) }
                    )
                }

                searchQuery.length >= 2 -> {
                    // ❌ SIN RESULTADOS
                    NoResultsContent(query = searchQuery)
                }
            }
        }
    }
}

/**
 * 🌟 CIUDADES POPULARES / FAVORITAS
 */
@Composable
fun PopularCitiesContent(
    cities: List<CityItem>,
    onCityClick: (String) -> Unit,
    onAddToFavorites: (CityItem) -> Unit
) {
    Column {
        Text(
            "🌟 Ciudades Populares",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(cities) { city ->
                CityCard(
                    city = city,
                    onClick = { onCityClick(city.name) },
                    onFavoriteClick = { onAddToFavorites(city) }
                )
            }
        }
    }
}

/**
 * 📋 RESULTADOS DE BÚSQUEDA
 */
@Composable
fun SearchResultsContent(
    results: List<CityItem>,
    onCityClick: (String) -> Unit,
    onAddToFavorites: (CityItem) -> Unit
) {
    Column {
        Text(
            "🔍 Resultados (${results.size})",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(results) { city ->
                CityCard(
                    city = city,
                    onClick = { onCityClick(city.name) },
                    onFavoriteClick = { onAddToFavorites(city) }
                )
            }
        }
    }
}

/**
 * 🏙️ TARJETA DE CIUDAD
 */
@Composable
fun CityCard(
    city: CityItem,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.15f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.8f),
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        city.name,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    if (city.country.isNotEmpty()) {
                        Text(
                            city.country,
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 12.sp
                        )
                    }
                }
            }

            // ⭐ Botón de favoritos
            IconButton(
                onClick = onFavoriteClick,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    Icons.Default.Star,
                    contentDescription = "Agregar a favoritos",
                    tint = if (city.isFavorite) Color(0xFFFFD700) else Color.White.copy(alpha = 0.5f),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

/**
 * 🔄 CONTENIDO DE BÚSQUEDA
 */
@Composable
fun SearchingContent() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            color = Color.White,
            strokeWidth = 3.dp,
            modifier = Modifier.size(48.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "🔍 Buscando ciudades...",
            color = Color.White,
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
    }
}

/**
 * ❌ SIN RESULTADOS
 */
@Composable
fun NoResultsContent(query: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "🤔",
            fontSize = 64.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "No encontramos \"$query\"",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            "Prueba con:\n• Nombre completo (ej: Madrid)\n• En inglés (ej: London)\n• Con país (ej: Paris, France)",
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
    }
}

/**
 * 🏙️ MODELO DE DATOS PARA CIUDADES
 */
data class CityItem(
    val name: String,
    val country: String = "",
    val isFavorite: Boolean = false,
    val emoji: String = "🏙️"
)