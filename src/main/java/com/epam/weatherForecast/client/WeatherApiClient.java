package com.epam.weatherForecast.client;

import com.epam.weatherForecast.dto.weatherApi.CurrentWeatherDto;
import com.epam.weatherForecast.dto.weatherApi.ForecastWeatherDto;

public interface WeatherApiClient {

    CurrentWeatherDto getCurrentWeatherByCityAndCountry(String country, String city);

    ForecastWeatherDto getWeatherForTodayEachHour(String country, String city);

    ForecastWeatherDto getTodayWeatherByHour(String country, String city, int hour);
}
