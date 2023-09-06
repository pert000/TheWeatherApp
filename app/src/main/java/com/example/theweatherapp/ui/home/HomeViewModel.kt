package com.example.theweatherapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.theweatherapp.data.Resource
import com.example.theweatherapp.data.remote.model.WeatherDto
import com.example.theweatherapp.domain.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: WeatherRepository
): ViewModel() {


    private val _weatherData = MutableLiveData<Resource<WeatherDto>>()
    val weatherData: LiveData<Resource<WeatherDto>> = _weatherData

    fun fetchWeatherData(lat: Double, lon: Double) {
        viewModelScope.launch {
            val result = repository.getWeatherData(lat, lon)
            _weatherData.value = result
        }
    }
}