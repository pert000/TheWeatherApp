package com.example.theweatherapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.theweatherapp.data.Resource
import com.example.theweatherapp.data.remote.model.WeatherDto
import com.example.theweatherapp.domain.location.LocationTracker
import com.example.theweatherapp.domain.repository.WeatherRepository
import com.example.theweatherapp.ui.helper.Constants.Constants.PERMISSION_NOT_GRANTED
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: WeatherRepository,
    private val locationTracker: LocationTracker
) : ViewModel() {


    private val _weatherData = MutableLiveData<Resource<WeatherDto>>()
    val weatherData: LiveData<Resource<WeatherDto>> = _weatherData


    fun fetchWeatherData(lat: Double? = null, lon: Double? = null) {


        viewModelScope.launch {
            if (lat != null && lon != null) {
                viewModelScope.launch {
                    val result = repository.getWeatherData(lat, lon)
                    _weatherData.value = result
                }

            } else {

                locationTracker.getCurrentLocation()?.let { location ->
                    when (val result =
                        repository.getWeatherData(location.latitude, location.longitude)) {
                        is Resource.Success -> {
                            _weatherData.value = result
                        }

                        is Resource.Error -> {
                            _weatherData.value =  Resource.Error("some unexpected happen")
                        }
                    }
                } ?: kotlin.run {
                    _weatherData.value = Resource.Error(PERMISSION_NOT_GRANTED)

                }
            }


        }
    }

}