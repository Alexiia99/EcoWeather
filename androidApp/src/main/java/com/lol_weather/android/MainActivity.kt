package com.lolweather.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.lolweather.android.di.appModule
import com.lolweather.android.presentation.CitySearchScreen
import com.lolweather.android.presentation.ForecastScreen
import com.lolweather.android.presentation.WeatherScreen
import com.lolweather.android.ui.theme.LolWeatherTheme
import di.sharedModule
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.compose.KoinAndroidContext
import org.koin.core.context.GlobalContext.startKoin

/**
 * 🎮 MainActivity con navegación épica entre:
 * - 🌤️ Clima actual (WeatherScreen)
 * - 📅 Pronóstico 5 días (ForecastScreen)
 * - 🔍 Búsqueda de ciudades (CitySearchScreen) ✨ NUEVO
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 🏗️ Inicializar Koin para inyección de dependencias
        startKoin {
            androidContext(this@MainActivity)
            modules(sharedModule, appModule)
        }

        setContent {
            KoinAndroidContext {
                LolWeatherTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        // 🚀 Sistema de navegación épico ACTUALIZADO
                        LoLWeatherApp()
                    }
                }
            }
        }
    }
}

/**
 * 🎮 App principal con navegación ACTUALIZADA
 * Gestiona la navegación entre todas las pantallas
 */
@Composable
fun LoLWeatherApp() {
    // 📱 Estado de navegación expandido
    var currentScreen by remember { mutableStateOf(Screen.Weather) }
    var selectedCity by remember { mutableStateOf<String?>(null) }

    when (currentScreen) {
        Screen.Weather -> {
            WeatherScreen(
                selectedCity = selectedCity, // 🔍 NUEVA: ciudad seleccionada
                onNavigateToForecast = {
                    currentScreen = Screen.Forecast
                },
                onNavigateToSearch = { // 🔍 NUEVA: ir a búsqueda
                    currentScreen = Screen.CitySearch
                }
            )
        }

        Screen.Forecast -> {
            ForecastScreen(
                selectedCity = selectedCity, // 🔍 NUEVA: ciudad seleccionada
                onBackClick = {
                    currentScreen = Screen.Weather
                }
            )
        }

        Screen.CitySearch -> { // 🔍 NUEVA PANTALLA
            CitySearchScreen(
                onBackClick = {
                    currentScreen = Screen.Weather
                },
                onCitySelected = { cityName ->
                    selectedCity = cityName
                    currentScreen = Screen.Weather
                }
            )
        }
    }
}

/**
 * 📱 Enum para las pantallas disponibles ACTUALIZADO
 */
enum class Screen {
    Weather,      // 🌤️ Clima actual
    Forecast,     // 📅 Pronóstico 5 días
    CitySearch    // 🔍 Búsqueda de ciudades ✨ NUEVO
}
