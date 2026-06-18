package au.edu.jcu.cp3406_cp5307_utilityappstartertemplate.data

class WeatherRepository(
    private val weatherApi: WeatherApi
) {
    suspend fun getHydrationSuggestion(): String {
        return try {
            val response = weatherApi.getCurrentWeather(
                latitude = -19.2590,
                longitude = 146.8169
            )

            val temperature = response.current?.temperature

            when {
                temperature == null -> "Weather data is unavailable. Keep drinking water regularly."
                temperature >= 30 -> "It is ${temperature.toInt()}°C. Warm weather today, so remember to hydrate regularly."
                temperature >= 24 -> "It is ${temperature.toInt()}°C. A steady water intake will help you stay refreshed."
                else -> "It is ${temperature.toInt()}°C. Keep your hydration goal going today."
            }
        } catch (exception: Exception) {
            "Unable to load weather tip. Keep tracking your water intake."
        }
    }
}