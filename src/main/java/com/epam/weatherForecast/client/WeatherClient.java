package com.epam.weatherForecast.client;

import com.epam.weatherForecast.model.Weather;

import java.util.List;

public interface WeatherClient {

    Weather getCurrentWeather(String country, String city);
    List<Weather> getWeatherForToday(String country, String city);
    Weather getTodayWeatherForHour(String country, String city, int hour);


}
