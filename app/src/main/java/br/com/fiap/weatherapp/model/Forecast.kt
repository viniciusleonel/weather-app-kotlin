package br.com.fiap.weatherapp.model

data class Forecast(
    val date: String,
    val weekday: String,
    val max: Int,
    val min: Int,
    val cloudiness: Double,
    val rain: Double,
    val rain_probability: Int,
    val wind_speedy: String,
    val description: String,
    val condition: String
)