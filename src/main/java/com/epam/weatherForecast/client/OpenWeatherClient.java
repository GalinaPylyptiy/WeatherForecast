package com.epam.weatherForecast.client;

import com.epam.weatherForecast.dto.openWeather.WeatherDto;
import com.epam.weatherForecast.dto.openWeather.WeatherListDto;

import java.util.List;

public interface OpenWeatherClient {

    WeatherDto getCurrentWeatherByCountryAndCity(String city, String country);

    WeatherListDto getWeatherForTodayEach3Hours (String country, String city);

}
