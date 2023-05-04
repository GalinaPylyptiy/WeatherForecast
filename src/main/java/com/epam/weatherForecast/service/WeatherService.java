package com.epam.weatherForecast.service;
import com.epam.weatherForecast.model.Weather;

import java.util.List;


public interface WeatherService {

    Weather getCurrentWeather(String city, String country);
    List<Weather> getWeatherForToday(String country, String city, boolean eachHour);
    Weather getTodayWeatherForHour(String country, String city, int hour);
    String compareTwoCitiesTemperature(String city1, String city2, String country);

}
