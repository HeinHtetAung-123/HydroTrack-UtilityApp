package au.edu.jcu.cp3406_cp5307_utilityappstartertemplate.data

import retrofit2.http.GET
import retrofit2.http.Query

// Retrofit interface for the Open-Meteo weather API.
// The app uses this API to fetch current temperature data for the hydration tip.
interface WeatherApi {

    // Calls the forecast endpoint and requests the current temperature.
    // The latitude and longitude values are supplied by the repository.
    @GET("v1/forecast")
    suspend fun getCurrentWeather(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("current") current: String = "temperature_2m",
        @Query("timezone") timezone: String = "auto"
    ): WeatherResponse
}