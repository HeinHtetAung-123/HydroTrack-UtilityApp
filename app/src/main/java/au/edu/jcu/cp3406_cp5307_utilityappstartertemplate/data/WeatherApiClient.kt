package au.edu.jcu.cp3406_cp5307_utilityappstartertemplate.data

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Creates and provides the Retrofit API service used for weather requests.
// Keeping this setup in one object avoids repeating Retrofit configuration elsewhere.
object WeatherApiClient {
    private const val BASE_URL = "https://api.open-meteo.com/"

    // Logs basic network request information, which is useful during development and testing.
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BASIC
    }

    // OkHttp client used by Retrofit. The logging interceptor helps show API calls in Logcat.
    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    // Lazy creation means Retrofit is only built when the API service is first needed.
    val weatherApi: WeatherApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApi::class.java)
    }
}