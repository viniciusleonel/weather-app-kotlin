package br.com.fiap.weatherapp.model

data class WeatherResults(
    val temp: Int,
    val date: String,
    val time: String,
    val condition_code: String,
    val description: String,
    val currently: String,
    val cid: String,
    val city: String,
    val img_id: String,
    val humidity: Int,
    val cloudiness: Double,
    val rain: Double,
    val wind_speedy: String,
    val wind_direction: Int,
    val wind_cardinal: String,
    val sunrise: String,
    val sunset: String,
    val moon_phase: String,
    val condition_slug: String,
    val city_name: String,
    val timezone: String,
    val forecast: List<Forecast>,
    val cref: String
)