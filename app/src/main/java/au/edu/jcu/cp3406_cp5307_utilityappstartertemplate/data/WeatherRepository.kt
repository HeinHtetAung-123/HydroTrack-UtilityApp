package au.edu.jcu.cp3406_cp5307_utilityappstartertemplate.data

// Repository for weather-related data.
// The ViewModel asks this repository for a hydration suggestion instead of calling Retrofit directly.
class WeatherRepository(
    private val weatherApi: WeatherApi
) {
    suspend fun getHydrationSuggestion(): String {
        return try {
            // Fixed coordinates are used for this project to keep the API feature simple.
            // These coordinates are for Townsville, which matches the JCU assignment context.
            val response = weatherApi.getCurrentWeather(
                latitude = -19.2590,
                longitude = 146.8169
            )

            val temperature = response.current?.temperature

            // Converts the API temperature result into a user-friendly hydration message.
            when {
                temperature == null -> "Weather data is unavailable. Keep drinking water regularly."
                temperature >= 30 -> "It is ${temperature.toInt()}°C. Warm weather today, so remember to hydrate regularly."
                temperature >= 24 -> "It is ${temperature.toInt()}°C. A steady water intake will help you stay refreshed."
                else -> "It is ${temperature.toInt()}°C. Keep your hydration goal going today."
            }
        } catch (exception: Exception) {
            // Fallback message shown if the API request fails or the device has no connection.
            "Unable to load weather tip. Keep tracking your water intake."
        }
    }
}