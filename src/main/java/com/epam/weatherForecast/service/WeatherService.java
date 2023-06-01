package com.epam.weatherForecast.service;
import com.epam.weatherForecast.model.Weather;

import java.util.Collection;


public interface WeatherService {

    Weather getCurrentWeather(String city, String country);
    Collection<Weather> getWeatherForToday(String country, String city, boolean eachHour);
    Weather getTodayWeatherForHour(String country, String city, int hour);
    String compareTwoCitiesTemperature(String city1, String city2, String country);

}
