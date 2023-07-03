package com.epam.weatherForecast.converter;

import com.epam.weatherForecast.client.WeatherApiClient;
import com.epam.weatherForecast.client.impl.WeatherApiClientImpl;
import com.epam.weatherForecast.dto.weatherApi.CurrentWeatherDto;
import com.epam.weatherForecast.dto.weatherApi.ForecastDay;
import com.epam.weatherForecast.dto.weatherApi.ForecastWeatherDto;
import com.epam.weatherForecast.dto.weatherApi.Hour;
import com.epam.weatherForecast.model.Weather;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;

@RequiredArgsConstructor
@Component
public class WeatherApiConverter {

    private final WeatherApiClient weatherApiClient;

    public Weather convertCurrentWeather(String city, String country) {
        CurrentWeatherDto currentWeatherDto = weatherApiClient.getCurrentWeatherByCityAndCountry(country, city);
        Weather weather = new Weather();
        weather.setTemperature(String.valueOf(currentWeatherDto.getCurrent().getTemperature()));
        weather.setFeelsLike(String.valueOf(currentWeatherDto.getCurrent().getFeelsLike()));
        weather.setDescription(currentWeatherDto.getCurrent().getCondition().getDescription());
        weather.setWindSpeed(String.valueOf(currentWeatherDto.getCurrent().getWindSpeed()));
        weather.setCity(city);
        weather.setCountry(country);
        weather.setDateAndTime(LocalDateTime.now());
        return weather;
    }

    public Weather convertWeatherForHour(String city, String country, int hour) {
        ForecastWeatherDto weatherDto = weatherApiClient.getTodayWeatherByHour(country, city, hour);
        Weather weather = new Weather();
        for (ForecastDay forecastday : weatherDto.getForecast().getForecastDay()) {
            forecastday.getHour().forEach(hourWeatherDto -> mapWeatherData(city, country, hourWeatherDto, weather));
        }
        return weather;
    }

    public Collection<Weather> convertWeatherForToday(String city, String country) {
        Collection<Weather> todayWeatherList = new ArrayList<>();
        ForecastWeatherDto weatherDto = weatherApiClient.getWeatherForTodayEachHour(country, city);
        for (ForecastDay forecastday : weatherDto.getForecast().getForecastDay()) {
            forecastday.getHour().forEach(hourWeatherDto -> {
                Weather weather = new Weather();
                mapWeatherData(city, country, hourWeatherDto, weather);
                todayWeatherList.add(weather);
            });
        }
        return todayWeatherList;
    }

    private void mapWeatherData(String city, String country, Hour hourWeatherDto, Weather weather) {
        String dateAndTimePattern = "yyyy-MM-dd HH:mm";
        weather.setCity(city);
        weather.setCountry(country);
        weather.setTemperature(String.valueOf(hourWeatherDto.getTemperature()));
        weather.setFeelsLike(String.valueOf(hourWeatherDto.getFeelsLike()));
        weather.setWindSpeed(String.valueOf(hourWeatherDto.getWindSpeed()));
        weather.setDescription(hourWeatherDto.getCondition().getDescription());
        weather.setDateAndTime(LocalDateTime.parse(hourWeatherDto.getDateAndTime(),
                DateTimeFormatter.ofPattern(dateAndTimePattern)));
    }
}
