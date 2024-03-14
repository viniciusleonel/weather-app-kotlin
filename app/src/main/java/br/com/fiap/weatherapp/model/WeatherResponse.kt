package br.com.fiap.weatherapp.model

data class WeatherResponse(
    val by: String,
    val valid_key: Boolean,
    val results: WeatherResults,
    val execution_time: Double,
    val from_cache: Boolean
)