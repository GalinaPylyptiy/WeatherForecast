package com.epam.weatherForecast.client;

import com.epam.weatherForecast.model.Weather;

public interface WeatherClient {

    Weather getCurrentWeather(String country, String city);
}
