package com.lolweather.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.lolweather.android.di.appModule
import com.lolweather.android.presentation.WeatherScreen
import com.lolweather.android.ui.theme.LolWeatherTheme
import di.sharedModule
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.compose.KoinAndroidContext
import org.koin.core.context.GlobalContext.startKoin

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
                        // 🎮 ¡Nuestra pantalla principal con emotes de LoL!
                        WeatherScreen()
                    }
                }
            }
        }
    }
}