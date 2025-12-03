package com.dewis.dm_huertohogar_ex3.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

data class WeatherResponse(
    val weather: List<WeatherDesc>,
    val main: WeatherMain,
    val name: String
)

data class WeatherDesc(
    val description: String,
    val icon: String
)

data class WeatherMain(
    val temp: Double,
    val humidity: Int
)

interface WeatherApi {

    @GET("data/2.5/weather")
    suspend fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "es"
    ): WeatherResponse
}
