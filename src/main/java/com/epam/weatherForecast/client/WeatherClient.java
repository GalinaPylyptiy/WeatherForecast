package com.epam.weatherForecast.client;

import com.epam.weatherForecast.model.Weather;
import java.util.Collection;

public interface WeatherClient {

    Weather getCurrentWeather(String country, String city);
    Collection<Weather> getWeatherForToday(String country, String city);
    Weather getTodayWeatherForHour(String country, String city, int hour);


}
