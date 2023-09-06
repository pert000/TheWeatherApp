package com.example.theweatherapp.data.repository

import com.example.theweatherapp.data.Resource
import com.example.theweatherapp.data.remote.GetWeatherApi
import com.example.theweatherapp.data.remote.model.WeatherDto
import com.example.theweatherapp.domain.repository.WeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val api: GetWeatherApi
) : WeatherRepository {

    override suspend fun getWeatherData(lat: Double, lon: Double): Resource<WeatherDto> {
        return try {
            Resource.Success(
                data = api.getWeatherData(
                    lat = lat,
                    lon = lon
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message ?: "An unknown error occurred.")
        }
    }

}