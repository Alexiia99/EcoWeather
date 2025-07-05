package com.lolweather.android.di

import com.lolweather.android.location.LocationManager
import com.lolweather.android.presentation.WeatherViewModel
import com.lolweather.android.presentation.ForecastViewModel
import com.lolweather.android.presentation.CitySearchViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * 🏗️ Módulo de dependencias específico para Android
 * ¡Ahora incluye LocationManager y TODOS los ViewModels! 📍🎮🔍
 */
val appModule = module {
    // 📍 Manager de ubicación
    single { LocationManager(androidContext()) }

    // 🧠 ViewModel del clima actual con inyección automática de dependencias
    viewModel { WeatherViewModel(get(), get()) }

    // 📅 ViewModel del pronóstico de 5 días
    viewModel {
        ForecastViewModel(
            getForecastUseCase = get(),           // Use case para pronóstico simple
            getCompleteWeatherUseCase = get(),    // Use case para pronóstico + clima actual
            locationManager = get()               // Manager de ubicación
        )
    }

    // 🔍 ViewModel de búsqueda de ciudades
    viewModel { CitySearchViewModel() }
}