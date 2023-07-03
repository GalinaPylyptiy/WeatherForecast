package com.epam.weatherForecast.externalWeatherService.impl;

import com.epam.weatherForecast.externalWeatherService.ExternalWeatherService;
import com.epam.weatherForecast.converter.OpenWeatherConverter;
import com.epam.weatherForecast.model.Weather;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@Component
public class OpenWeatherService implements ExternalWeatherService {

    private final OpenWeatherConverter converter;

    public Weather getCurrentWeather(String city, String country) {
        return converter.convertCurrentWeather(city, country);
    }

    @Override
    public Collection<Weather> getWeatherForToday(String country, String city) {
        List<Weather> todayWeatherListEach3Hours = converter.convertWeatherListForToday(city, country);
        return convertWeatherForTodayToEachHour(todayWeatherListEach3Hours);
    }

    @Override
    public Weather getTodayWeatherForHour(String country, String city, int hour) {
        Collection<Weather> todayWeather = getWeatherForToday(country, city);
        return todayWeather.stream()
                .filter(weather -> weather.getDateAndTime().getHour() == hour)
                .findFirst()
                .orElse(getCurrentWeather(city, country));
    }

    private List<Weather> convertWeatherForTodayToEachHour(List<Weather> each3HourWeather) {
        LocalDate today = LocalDate.now();
        List<Weather> resultList = new ArrayList<>();
        for (Weather weather : each3HourWeather) {
            int hourToAdd = 0;
            resultList.add(getNewWeatherFromSourceWithNewTime(weather, today, hourToAdd));
            resultList.add(getNewWeatherFromSourceWithNewTime(weather, today, ++hourToAdd));
            resultList.add(getNewWeatherFromSourceWithNewTime(weather, today, ++hourToAdd));
        }
        return resultList;
    }

    private Weather getNewWeatherFromSourceWithNewTime(Weather source, LocalDate today, int hour) {
        Weather weatherResult = source.cloneWeatherWithoutDateAndTime();
        weatherResult.setDateAndTime(LocalDateTime.of(today, source.getDateAndTime().toLocalTime().plusHours(hour)));
        return weatherResult;
    }

//    TODO test

}
