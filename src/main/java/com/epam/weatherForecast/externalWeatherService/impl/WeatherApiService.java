package com.epam.weatherForecast.externalWeatherService.impl;
import com.epam.weatherForecast.externalWeatherService.ExternalWeatherService;
import com.epam.weatherForecast.converter.WeatherApiConverter;
import com.epam.weatherForecast.model.Weather;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

@RequiredArgsConstructor
@Component
public class WeatherApiService implements ExternalWeatherService {

    private final WeatherApiConverter converter;

    @Override
    public Weather getCurrentWeather(String country, String city) {
        return converter.convertCurrentWeather(city, country);
    }

    @Override
    public Collection<Weather> getWeatherForToday(String country, String city) {
        return converter.convertWeatherForToday(city,country);
    }

    @Override
    public Weather getTodayWeatherForHour(String country, String city, int hour) {
        return converter.convertWeatherForHour(city, country, hour);
    }
}
