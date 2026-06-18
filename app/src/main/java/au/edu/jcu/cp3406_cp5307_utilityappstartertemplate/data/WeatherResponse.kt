package au.edu.jcu.cp3406_cp5307_utilityappstartertemplate.data

import com.google.gson.annotations.SerializedName

// Represents the main response returned from the Open-Meteo API.
// Only the "current" section is needed because HydroTrack uses the current temperature.
data class WeatherResponse(
    @SerializedName("current")
    val current: CurrentWeather?
)

// Represents the current weather values used by the app.
// SerializedName maps the JSON field "temperature_2m" to the Kotlin property "temperature".
data class CurrentWeather(
    @SerializedName("temperature_2m")
    val temperature: Double?
)