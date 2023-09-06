package com.example.theweatherapp.di

import com.example.theweatherapp.data.repository.WeatherRepositoryImpl
import com.example.theweatherapp.domain.repository.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun provideCurrentWeatherRepo(impl : WeatherRepositoryImpl) : WeatherRepository

}