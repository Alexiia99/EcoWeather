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
                        views.setTextViewText(R.id.widget_city, weatherInfo.cityName)
                        views.setTextViewText(R.id.widget_temperature, "${weatherInfo.temperature.toInt()}°")
                        views.setTextViewText(R.id.widget_description, weatherInfo.description)
                        views.setTextViewText(R.id.widget_emote, getEmoteForWidget(weatherInfo.temperature))
                        views.setTextViewText(R.id.widget_humidity, "${weatherInfo.humidity}%")
                        views.setTextViewText(R.id.widget_wind, "${weatherInfo.windSpeed.toInt()} km/h")

                        val currentTime = java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault())
                            .format(java.util.Date())
                        views.setTextViewText(R.id.widget_last_update, "Actualizado: $currentTime")

                        val backgroundColor = getWidgetBackgroundColor(weatherInfo.temperature)
                        views.setInt(R.id.widget_container, "setBackgroundColor", backgroundColor)

                        appWidgetManager.updateAppWidget(appWidgetId, views)
                    }
                }.onFailure {
                    CoroutineScope(Dispatchers.Main).launch {
                        views.setTextViewText(R.id.widget_city, "Error")
                        views.setTextViewText(R.id.widget_temperature, "--°")
                        views.setTextViewText(R.id.widget_description, "Sin conexión")
                        views.setTextViewText(R.id.widget_emote, "😵")
                        appWidgetManager.updateAppWidget(appWidgetId, views)
                    }
                }
            } catch (e: Exception) {
                CoroutineScope(Dispatchers.Main).launch {
                    views.setTextViewText(R.id.widget_city, "Error")
                    views.setTextViewText(R.id.widget_temperature, "--°")
                    appWidgetManager.updateAppWidget(appWidgetId, views)
                }
            }
        }

        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    private fun getEmoteForWidget(temperature: Double): String {
        return when {
            temperature < 0 -> "🥶"
            temperature < 10 -> "❄️"
            temperature < 18 -> "😊"
            temperature < 25 -> "😎"
            temperature < 32 -> "😅"
            temperature < 38 -> "🥵"
            else -> "🔥"
        }
    }

    private fun getWidgetBackgroundColor(temperature: Double): Int {
        return when {
            temperature < 0 -> 0xFF0D47A1.toInt()
            temperature < 10 -> 0xFF1565C0.toInt()
            temperature < 18 -> 0xFF0277BD.toInt()
            temperature < 25 -> 0xFF2E7D32.toInt()
            temperature < 32 -> 0xFFF57F17.toInt()
            temperature < 38 -> 0xFFE65100.toInt()
            else -> 0xFFD84315.toInt()
        }
    }
}