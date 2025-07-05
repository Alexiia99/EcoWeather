package domain.usecase

import data.repository.WeatherRepositoryImpl
import domain.model.WeatherInfo
import domain.model.WeekForecast
import domain.repository.WeatherRepository

/**
 * 🌤️ Use Case para obtener clima actual
 */
class GetWeatherUseCase(private val repository: WeatherRepository) {

    suspend fun byLocation(lat: Double, lon: Double): Result<WeatherInfo> {
        return repository.getWeatherByLocation(lat, lon)
    }

    suspend fun byCity(cityName: String): Result<WeatherInfo> {
        return repository.getWeatherByCity(cityName)
    }
}

/**
 * 📅 Use Case para obtener pronóstico de 5 días
 */
class GetForecastUseCase(private val repository: WeatherRepository) {

    suspend fun byLocation(lat: Double, lon: Double): Result<WeekForecast> {
        return repository.getForecastByLocation(lat, lon)
    }

    suspend fun byCity(cityName: String): Result<WeekForecast> {
        return repository.getForecastByCity(cityName)
    }
}

/**
 * 🚀 Use Case para obtener clima completo (actual + pronóstico)
 */
class GetCompleteWeatherUseCase(private val repository: WeatherRepository) {

    suspend fun byLocation(lat: Double, lon: Double): Result<WeekForecast> {
        return repository.getCompleteWeatherByLocation(lat, lon)
    }

    suspend fun byCity(cityName: String): Result<WeekForecast> {
        return repository.getCompleteWeatherByCity(cityName)
    }
}