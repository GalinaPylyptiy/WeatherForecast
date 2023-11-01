package com.epam.weatherForecast.externalWeatherService.impl;

import com.epam.weatherForecast.dto.openWeather.WeatherDto;
import com.epam.weatherForecast.dto.openWeather.WeatherListDto;
import com.epam.weatherForecast.externalWeatherService.ExternalWeatherService;
import com.epam.weatherForecast.feignClient.OpenWeatherFeignClient;
import com.epam.weatherForecast.mapper.OpenWeatherMapper;
import com.epam.weatherForecast.model.Weather;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@Component
@PropertySource("classpath:value.properties")
public class OpenWeatherService implements ExternalWeatherService {

    private final OpenWeatherMapper openWeatherMapper;
    private final OpenWeatherFeignClient weatherFeignClient;
    @Value("${openWeatherApiKey}")
    private String apiKey;
    private String descriptionErrorMsg = "Unable to get description ";

    public Weather getCurrentWeather(String city, String country) {
        WeatherDto weatherDto = weatherFeignClient.getCurrentWeather(List.of(city, country), apiKey);
        Weather weather = openWeatherMapper.toCurrentWeather(weatherDto, city, country);
        weather.setDescription(weatherDto.getWeather()
                .stream()
                .map(com.epam.weatherForecast.dto.openWeather.Weather::getDescription)
                .findFirst()
                .orElse(descriptionErrorMsg));
        return weather;
    }

    @Override
    public Collection<Weather> getWeatherForToday(String country, String city) {
        WeatherListDto weatherListDto = weatherFeignClient.getWeatherForTodayEach3Hours(List.of(city, country), apiKey);
        List<Weather> todayWeatherListEach3Hours = openWeatherMapper
                .toWeatherListForToday((List<WeatherDto>) weatherListDto.getList());
        todayWeatherListEach3Hours
                .forEach(weather -> {
                    weather.setCity(city);
                    weather.setCountry(country);
                    weather.setDescription(weatherListDto.getList()
                            .stream()
                            .map(WeatherDto::getWeather)
                            .flatMap(Collection::stream)
                            .map(com.epam.weatherForecast.dto.openWeather.Weather::getDescription)
                            .findFirst()
                            .orElse(descriptionErrorMsg));
                });
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


}
