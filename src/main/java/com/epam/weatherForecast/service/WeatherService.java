package com.epam.weatherForecast.service;
import com.epam.weatherForecast.model.Weather;


public interface WeatherService {

    Weather getCurrentWeather(String city, String country);

}
