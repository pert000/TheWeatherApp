package com.example.theweatherapp.domain.repository

import com.example.theweatherapp.data.Resource
import com.example.theweatherapp.data.remote.model.WeatherDto


interface WeatherRepository {
    suspend fun getWeatherData(lat: Double, long: Double): Resource<WeatherDto>
}