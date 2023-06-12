package com.epam.weatherForecast.service.impl;

import com.epam.weatherForecast.externalWeatherService.ExternalWeatherService;
import com.epam.weatherForecast.model.Weather;
import com.epam.weatherForecast.service.WeatherService;
import com.epam.weatherForecast.util.AverageValueCalculator;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class WeatherServiceImpl implements WeatherService {

    private final Collection<ExternalWeatherService> externalWeatherServices;
    private final AverageValueCalculator calculator;

    public WeatherServiceImpl(Collection<ExternalWeatherService> externalWeatherServices, AverageValueCalculator calculator) {
        this.externalWeatherServices = externalWeatherServices;
        this.calculator = calculator;
    }

    @Override
    public Weather getCurrentWeather(String city, String country) {
        Collection<Weather> currentWeatherList = getCurrentWeatherList(country, city);
        LocalDateTime now = LocalDateTime.now();
        String averageTemp = convertToString(calculator.calculateAverageValue(currentWeatherList, Weather::getTemperature));
        String averageFeelsLike = convertToString(calculator.calculateAverageValue(currentWeatherList, Weather::getFeelsLike));
        String averageWindSpeed = convertToString(calculator.calculateAverageValue(currentWeatherList, Weather::getWindSpeed));
        String description = getDescription(currentWeatherList);
        return new Weather(city, country, now, averageTemp,
                averageFeelsLike, averageWindSpeed, description);
    }

    @Override
    public Collection<Weather> getWeatherForToday(String country, String city) {
        return getAvgWeatherForToday(country, city);
    }

    @Override
    public Weather getTodayWeatherForHour(String country, String city, int hour) {
        if (hour < 0 || hour >= 24) {
            throw new IllegalArgumentException("Inserted hour should be from  0 to 23");
        }
        Collection<Weather> hourWeatherList = getHourWeatherList(country, city, hour);
        Weather hourWeather = setAvgWeatherDataWithoutDateAndTime(city, country, hourWeatherList);
        hourWeather.setDateAndTime(getDateAndTimeWithHour(hour));
        return hourWeather;
    }

    @Override
    public String compareTwoCitiesTemperature(String city1, String city2, String country) {
        Weather weatherCity1 = getCurrentWeather(city1, country);
        Weather weatherCity2 = getCurrentWeather(city2, country);
        int temp1 = Integer.parseInt(weatherCity1.getTemperature());
        int temp2 = Integer.parseInt(weatherCity2.getTemperature());
        if (temp1 > temp2)
            return "Weather in " + city1 + " is higher on " + (temp1 - temp2) + "°C";
        return "Weather in " + city2 + " is higher on " + (temp2 - temp1) + "°C";
    }

    private Collection<Weather> getCurrentWeatherList(String country, String city) {
        return externalWeatherServices.stream()
                .map(externalWeatherService -> externalWeatherService.getCurrentWeather(city, country))
                .collect(Collectors.toList());
    }

    private Collection<Weather> getHourWeatherList(String country, String city, int hour) {
        return externalWeatherServices.stream()
                .map(externalWeatherService -> externalWeatherService.getTodayWeatherForHour(city, country, hour))
                .collect(Collectors.toList());
    }

    private List<Collection<Weather>> getWeatherListForToday(String country, String city) {
        return externalWeatherServices.stream()
                .map(externalWeatherService -> externalWeatherService.getWeatherForToday(country, city))
                .collect(Collectors.toList());
    }

    private Collection<Weather> getAvgWeatherForToday(String country, String city) {
        return getWeatherListForToday(country, city)
                .stream()
                .flatMap(Collection::stream)
                .collect(Collectors.groupingBy(Weather::getDateAndTime))
                .entrySet()
                .stream()
                .map(entry -> {
                    Weather weather = setAvgWeatherDataWithoutDateAndTime(city, country, entry.getValue());
                    weather.setDateAndTime(entry.getKey());
                    return weather;
                })
                .sorted(Comparator.comparingInt(weather -> weather.getDateAndTime().getHour()))
                .collect(Collectors.toList());
    }

    private String getDescription(Collection<Weather> weatherList) {
        return weatherList.stream()
                .map(Weather::getDescription)
                .findAny()
                .orElse("Description is undefined");
    }

    private LocalDateTime getDateAndTimeWithHour(int hour) {
        LocalDate localDate = LocalDate.now();
        LocalTime localTime = LocalTime.of(hour, 0, 0);
        return LocalDateTime.of(localDate, localTime);
    }

    private Weather setAvgWeatherDataWithoutDateAndTime(String city, String country, Collection<Weather> weatherList){
        Weather weather = new Weather();
        weather.setCity(city);
        weather.setCountry(country);
        weather.setTemperature(convertToString(calculator.calculateAverageValue(weatherList, Weather::getTemperature)));
        weather.setFeelsLike(convertToString(calculator.calculateAverageValue(weatherList, Weather::getFeelsLike)));
        weather.setWindSpeed(convertToString(calculator.calculateAverageValue(weatherList, Weather::getWindSpeed)));
        weather.setDescription(getDescription(weatherList));
        return weather;
    }

    private String convertToString(int value) {
        return String.valueOf(value);
    }

}

//TODO Lombok
//TODO MupStruct
