package au.edu.jcu.cp3406_cp5307_utilityappstartertemplate.data

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("v1/forecast")
    suspend fun getCurrentWeather(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("current") current: String = "temperature_2m",
        @Query("timezone") timezone: String = "auto"
    ): WeatherResponse
}