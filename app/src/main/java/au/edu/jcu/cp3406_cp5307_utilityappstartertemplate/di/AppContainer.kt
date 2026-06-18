package au.edu.jcu.cp3406_cp5307_utilityappstartertemplate.di

import au.edu.jcu.cp3406_cp5307_utilityappstartertemplate.data.WeatherApiClient
import au.edu.jcu.cp3406_cp5307_utilityappstartertemplate.data.WeatherRepository

object AppContainer {
    val weatherRepository: WeatherRepository by lazy {
        WeatherRepository(
            weatherApi = WeatherApiClient.weatherApi
        )
    }
}