package com.example.theweatherapp.data.remote

import com.example.theweatherapp.data.remote.model.WeatherDto
import retrofit2.http.GET
import retrofit2.http.Query

interface GetWeatherApi {


    @GET("data/2.5/weather")
    suspend fun getWeatherData(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String = BaseURL.apiKey
    ): WeatherDto
}