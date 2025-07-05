package di

import data.mapper.ForecastMapper
import data.mapper.LolEmoteMapper
import data.remote.WeatherApi
import data.repository.WeatherRepositoryImpl
import domain.repository.WeatherRepository
import domain.usecase.GetCompleteWeatherUseCase
import domain.usecase.GetForecastUseCase
import domain.usecase.GetWeatherUseCase
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.dsl.module

/**
 * 🏗️ Módulo de dependencias compartido - CORREGIDO
 */
val sharedModule = module {

    // 🌐 Cliente HTTP para hacer requests a la API
    single {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true // Ignora campos desconocidos de la API
                    isLenient = true // Más flexible con el parsing
                })
            }
        }
    }

    // 🌤️ API del clima
    single { WeatherApi(get()) }

    // 🎮 Mapper de emotes de LoL - ¡La estrella del show!
    single { LolEmoteMapper() }

    // 📅 Mapper de pronóstico - Convierte API en modelo con emotes
    single { ForecastMapper(get()) }

    // 📦 Repository - conecta API con emotes
    single<WeatherRepository> { WeatherRepositoryImpl(get(), get()) }

    // 🎯 Use Cases - lo que usará la UI
    single { GetWeatherUseCase(get()) }           // Clima actual
    single { GetForecastUseCase(get()) }          // Pronóstico 5 días
    single { GetCompleteWeatherUseCase(get()) }   // ¡Todo junto!
}