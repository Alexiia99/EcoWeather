package com.lolweather.android.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.lolweather.android.MainActivity
import com.lolweather.android.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import domain.usecase.GetWeatherUseCase

class WeatherWidgetProvider : AppWidgetProvider(), KoinComponent {

    private val getWeatherUseCase: GetWeatherUseCase by inject()

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        val views = RemoteViews(context.packageName, R.layout.weather_widget)

        // Intent para abrir la app
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        views.setOnClickPendingIntent(R.id.widget_container, pendingIntent)

        // Intent para actualizar
        val updateIntent = Intent(context, WeatherWidgetProvider::class.java).apply {
            action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, intArrayOf(appWidgetId))
        }
        val updatePendingIntent = PendingIntent.getBroadcast(
            context, appWidgetId, updateIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        views.setOnClickPendingIntent(R.id.widget_refresh_button, updatePendingIntent)

        // Obtener clima
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = getWeatherUseCase.byCity("Valencia")
                result.onSuccess { weatherInfo ->
                    CoroutineScope(Dispatchers.Main).launch {
                        // 🏙️ Ciudad
                        views.setTextViewText(R.id.widget_city, weatherInfo.cityName)

                        // 🌡️ Temperatura principal
                        views.setTextViewText(R.id.widget_temperature, "${weatherInfo.temperature.toInt()}°")

                        // 🌤️ Descripción
                        views.setTextViewText(R.id.widget_description, weatherInfo.description)

                        // 🎮 Emote mejorado
                        views.setTextViewText(R.id.widget_emote, getEnhancedEmoteForWidget(weatherInfo.temperature))

                        // 💧 Humedad
                        views.setTextViewText(R.id.widget_humidity, "${weatherInfo.humidity}%")

                        // 💨 Viento
                        views.setTextViewText(R.id.widget_wind, "${weatherInfo.windSpeed.toInt()} km/h")

                        // 🔄 Sensación térmica (nuevo)
                      //  views.setTextViewText(R.id.widget_feels_like, "${weatherInfo.feelsLike.toInt()}°")

                        // ⏰ Última actualización
                        val currentTime = java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault())
                            .format(java.util.Date())
                        views.setTextViewText(R.id.widget_last_update, "Actualizado: $currentTime")

                        // 🎨 COLORES MEJORADOS - Gradientes suaves
                        val backgroundColor = getEnhancedWidgetBackgroundColor(weatherInfo.temperature)
                        views.setInt(R.id.widget_container, "setBackgroundColor", backgroundColor)

                        // 🎨 Color del texto principal según temperatura
                        val textColor = getWidgetTextColor(weatherInfo.temperature)
                        views.setTextColor(R.id.widget_temperature, textColor)
                        views.setTextColor(R.id.widget_city, textColor)

                        appWidgetManager.updateAppWidget(appWidgetId, views)
                    }
                }.onFailure { error ->
                    CoroutineScope(Dispatchers.Main).launch {
                        // 😵 Estado de error mejorado
                        views.setTextViewText(R.id.widget_city, "Error de conexión")
                        views.setTextViewText(R.id.widget_temperature, "--°")
                        views.setTextViewText(R.id.widget_description, "Toca para reintentar")
                        views.setTextViewText(R.id.widget_emote, "😵")
                        views.setTextViewText(R.id.widget_humidity, "--%")
                        views.setTextViewText(R.id.widget_wind, "-- km/h")

                        // Color de error
                        views.setInt(R.id.widget_container, "setBackgroundColor", 0xFF6B7280.toInt())
                        views.setTextColor(R.id.widget_temperature, 0xFFFFFFFF.toInt())
                        views.setTextColor(R.id.widget_city, 0xFFFFFFFF.toInt())

                        appWidgetManager.updateAppWidget(appWidgetId, views)
                    }
                }
            } catch (e: Exception) {
                CoroutineScope(Dispatchers.Main).launch {
                    // 🔥 Estado de excepción
                    views.setTextViewText(R.id.widget_city, "Error")
                    views.setTextViewText(R.id.widget_temperature, "--°")
                    views.setTextViewText(R.id.widget_description, "Error interno")
                    views.setTextViewText(R.id.widget_emote, "⚠️")

                    // Color de error crítico
                    views.setInt(R.id.widget_container, "setBackgroundColor", 0xFFDC2626.toInt())
                    views.setTextColor(R.id.widget_temperature, 0xFFFFFFFF.toInt())
                    views.setTextColor(R.id.widget_city, 0xFFFFFFFF.toInt())

                    appWidgetManager.updateAppWidget(appWidgetId, views)
                }
            }
        }

        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    /**
     * 🎮 Emotes mejorados con más variedad
     */
    private fun getEnhancedEmoteForWidget(temperature: Double): String {
        return when {
            temperature < -5 -> "🧊"  // Congelado extremo
            temperature < 0 -> "🥶"   // Muy frío
            temperature < 5 -> "🐧"   // Frío extremo
            temperature < 10 -> "❄️"  // Frío
            temperature < 15 -> "😌"  // Fresco
            temperature < 20 -> "😊"  // Agradable
            temperature < 25 -> "😎"  // Perfecto
            temperature < 28 -> "🙂"  // Cálido
            temperature < 32 -> "😅"  // Calorcito
            temperature < 35 -> "🥵"  // Calor
            temperature < 38 -> "🔥"  // Mucho calor
            else -> "🌋"              // Extremo
        }
    }

    /**
     * 🎨 Colores de fondo MUCHO más elegantes
     */
    private fun getEnhancedWidgetBackgroundColor(temperature: Double): Int {
        return when {
            // ❄️ Azules suaves para frío
            temperature < 0 -> 0xFF3B82F6.toInt()   // Blue-500
            temperature < 10 -> 0xFF1D4ED8.toInt()  // Blue-700
            temperature < 18 -> 0xFF0EA5E9.toInt()  // Sky-500

            // 🌿 Verdes para temperaturas perfectas
            temperature < 25 -> 0xFF10B981.toInt()  // Emerald-500
            temperature < 28 -> 0xFF059669.toInt()  // Emerald-600

            // 🌅 Naranjas suaves para calor moderado
            temperature < 32 -> 0xFFF59E0B.toInt()  // Amber-500
            temperature < 35 -> 0xFFEA580C.toInt()  // Orange-600

            // 🔥 Rojos elegantes para calor intenso
            temperature < 38 -> 0xFFDC2626.toInt()  // Red-600
            else -> 0xFF991B1B.toInt()              // Red-800
        }
    }

    /**
     * 🎨 Color del texto según temperatura (para mejor contraste)
     */
    private fun getWidgetTextColor(temperature: Double): Int {
        return when {
            // Texto blanco para fondos oscuros
            temperature < 0 || temperature > 32 -> 0xFFFFFFFF.toInt()
            // Texto negro para fondos claros
            else -> 0xFF1F2937.toInt() // Gray-800
        }
    }

    /**
     * 🎯 Descripción mejorada del emote
     */
    private fun getEmoteDescription(temperature: Double): String {
        return when {
            temperature < 0 -> "Congelándote"
            temperature < 10 -> "Frío"
            temperature < 18 -> "Fresco"
            temperature < 25 -> "Perfecto"
            temperature < 32 -> "Calorcito"
            temperature < 38 -> "Calor"
            else -> "Te derrites"
        }
    }
}