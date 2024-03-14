package service

import br.com.fiap.weatherapp.model.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("weather")
    fun getWeather(
        @Query("key") key: String,
        @Query("city_name") cityName: String
    ): Call<WeatherResponse>
}