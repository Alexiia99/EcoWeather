package com.lolweather.android.di

import com.lolweather.android.location.LocationManager
import com.lolweather.android.presentation.WeatherViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * 🏗️ Módulo de dependencias específico para Android
 * Ahora incluye LocationManager para geolocalización! 📍
 */
val appModule = module {
    // 📍 Manager de ubicación
    single { LocationManager(androidContext()) }

    // 🧠 ViewModel del clima con inyección automática de dependencias
    viewModel { WeatherViewModel(get(), get()) }
}