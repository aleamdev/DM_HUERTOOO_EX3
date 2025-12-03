package com.dewis.dm_huertohogar_ex3.data.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object ApiClient {

    // IMPORTANTE: HTTP, no HTTPS
    private const val BASE_URL = "http://192.168.100.18:8080/" // Cambiar por la ip del computador ejecutado

    private val okHttp = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttp)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val weatherApi: WeatherApi = Retrofit.Builder()
        .baseUrl("https://api.openweathermap.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(WeatherApi::class.java)


    val authApi: AuthApi = retrofit.create(AuthApi::class.java)
    val productApi: ProductApi = retrofit.create(ProductApi::class.java)
    val cartApi: CartApi = retrofit.create(CartApi::class.java)
}
