package au.edu.jcu.cp3406_cp5307_utilityappstartertemplate.di

import au.edu.jcu.cp3406_cp5307_utilityappstartertemplate.data.WeatherApiClient
import au.edu.jcu.cp3406_cp5307_utilityappstartertemplate.data.WeatherRepository

// Simple manual dependency injection container for the app.
// It provides shared dependencies from one place instead of creating them inside UI classes.
object AppContainer {

    // WeatherRepository is created lazily so it is only initialised when the ViewModel needs it.
    val weatherRepository: WeatherRepository by lazy {
        WeatherRepository(
            weatherApi = WeatherApiClient.weatherApi
        )
    }
}