package data.repository

import data.mapper.ForecastMapper
import data.remote.WeatherApi
import domain.model.*
import domain.repository.WeatherRepository

class WeatherRepositoryImpl(
    private val weatherApi: WeatherApi,
    private val forecastMapper: ForecastMapper
) : WeatherRepository {

    // 🌤️ ===== CLIMA ACTUAL =====

    override suspend fun getWeatherByLocation(lat: Double, lon: Double): Result<WeatherInfo> {
        return try {
            val response = weatherApi.getWeatherByCoordinates(lat, lon)
            val weatherInfo = forecastMapper.mapToWeatherInfo(response)
            Result.success(weatherInfo)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getWeatherByCity(cityName: String): Result<WeatherInfo> {
        return try {
            val response = weatherApi.getWeatherByCity(cityName)
            val weatherInfo = forecastMapper.mapToWeatherInfo(response)
            Result.success(weatherInfo)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // 📅 ===== PRONÓSTICO DE 5 DÍAS =====

    override suspend fun getForecastByLocation(lat: Double, lon: Double): Result<WeekForecast> {
        return try {
            val forecastResponse = weatherApi.getForecastByCoordinates(lat, lon)
            val weekForecast = forecastMapper.mapToWeekForecast(forecastResponse)
            Result.success(weekForecast)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getForecastByCity(cityName: String): Result<WeekForecast> {
        return try {
            val forecastResponse = weatherApi.getForecastByCity(cityName)
            val weekForecast = forecastMapper.mapToWeekForecast(forecastResponse)
            Result.success(weekForecast)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // 🚀 ===== CLIMA COMPLETO (ACTUAL + PRONÓSTICO) =====

    override suspend fun getCompleteWeatherByLocation(lat: Double, lon: Double): Result<WeekForecast> {
        return try {
            val (currentResponse, forecastResponse) = weatherApi.getCompleteWeatherByCoordinates(lat, lon)
            val currentWeather = forecastMapper.mapToWeatherInfo(currentResponse)
            val weekForecast = forecastMapper.mapToWeekForecast(forecastResponse, currentWeather)
            Result.success(weekForecast)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getCompleteWeatherByCity(cityName: String): Result<WeekForecast> {
        return try {
            val (currentResponse, forecastResponse) = weatherApi.getCompleteWeatherByCity(cityName)
            val currentWeather = forecastMapper.mapToWeatherInfo(currentResponse)
            val weekForecast = forecastMapper.mapToWeekForecast(forecastResponse, currentWeather)
            Result.success(weekForecast)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}