package com.epam.weatherForecast.converter;

import com.epam.weatherForecast.client.OpenWeatherClient;
import com.epam.weatherForecast.client.impl.OpenWeatherClientImpl;
import com.epam.weatherForecast.dto.openWeather.WeatherDto;
import com.epam.weatherForecast.dto.openWeather.WeatherListDto;
import com.epam.weatherForecast.model.Weather;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class OpenWeatherConverter {

    private final OpenWeatherClient weatherClient;

    public Weather convertCurrentWeather(String city, String country) {
        WeatherDto weatherDto = weatherClient.getCurrentWeatherByCountryAndCity(city, country);
        Weather weather = new Weather();
        mapWeatherDataWithoutDateAndTime(city, country, weatherDto, weather);
        weather.setDateAndTime(LocalDateTime.now());
        return weather;
    }

    public List<Weather> convertWeatherListForToday(String city, String country) {
        List<Weather> todayWeatherList = new ArrayList<>();
        String dateAndTimePattern = "yyyy-MM-dd HH:mm:ss";
        WeatherListDto weatherListDto = weatherClient.getWeatherForTodayEach3Hours(country, city);
        for (WeatherDto weatherDto : weatherListDto.getList()) {
            Weather weather = new Weather();
            mapWeatherDataWithoutDateAndTime(city, country, weatherDto, weather);
            weather.setDateAndTime(LocalDateTime.parse(weatherDto.getDateAndTime(),
                    DateTimeFormatter.ofPattern(dateAndTimePattern)));
            todayWeatherList.add(weather);
        }
        return todayWeatherList;
    }

    private void mapWeatherDataWithoutDateAndTime(String city, String country, WeatherDto weatherDto, Weather weather) {
        weather.setCity(city);
        weather.setCountry(country);
        weather.setTemperature(String.valueOf(weatherDto.getMain().getTemp()));
        weather.setFeelsLike(String.valueOf(weatherDto.getMain().getFeelsLike()));
        weather.setWindSpeed(String.valueOf(weatherDto.getWind().getSpeed()));
        weatherDto.getWeather().forEach(desc -> weather.setDescription(desc.getDescription()));
    }
}
