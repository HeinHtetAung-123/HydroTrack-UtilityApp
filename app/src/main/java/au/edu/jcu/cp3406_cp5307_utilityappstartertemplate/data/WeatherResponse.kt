package au.edu.jcu.cp3406_cp5307_utilityappstartertemplate.data

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("current")
    val current: CurrentWeather?
)

data class CurrentWeather(
    @SerializedName("temperature_2m")
    val temperature: Double?
)