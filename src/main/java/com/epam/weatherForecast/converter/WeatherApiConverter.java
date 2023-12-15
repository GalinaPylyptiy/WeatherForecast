package com.epam.weatherForecast.converter;

import com.epam.weatherForecast.client.WeatherApiClient;
import com.epam.weatherForecast.dto.weatherApi.CurrentWeatherDto;
import com.epam.weatherForecast.dto.weatherApi.ForecastDay;
import com.epam.weatherForecast.dto.weatherApi.ForecastWeatherDto;
import com.epam.weatherForecast.dto.weatherApi.Hour;
import com.epam.weatherForecast.feignClient.WeatherApiFeignClient;
import com.epam.weatherForecast.model.Weather;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@PropertySource("classpath:value.properties")
@Component
public class WeatherApiConverter {

    private final WeatherApiClient weatherApiClient;
    private final WeatherApiFeignClient feignClient;
    @Value("${weatherAPIKey}")
    private String API_KEY;

    public Weather convertCurrentWeather(String city, String country) {
//        CurrentWeatherDto currentWeatherDto = weatherApiClient.getCurrentWeatherByCityAndCountry(country, city);
        CurrentWeatherDto currentWeatherDto = feignClient.getCurrentWeatherByCityAndCountry(
                List.of(country, city),
                API_KEY);
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
//        ForecastWeatherDto weatherDto = weatherApiClient.getTodayWeatherByHour(country, city, hour);
        ForecastWeatherDto weatherDto = feignClient.getTodayWeatherByHour(
                List.of(country, city),
                API_KEY,
                hour);
        Weather weather = new Weather();
        for (ForecastDay forecastday : weatherDto.getForecast().getForecastDay()) {
            forecastday.getHour().forEach(hourWeatherDto -> mapWeatherData(city, country, hourWeatherDto, weather));
        }
        return weather;
    }

    public Collection<Weather> convertWeatherForToday(String city, String country) {
        Collection<Weather> todayWeatherList = new ArrayList<>();
//        ForecastWeatherDto weatherDto = weatherApiClient.getWeatherForTodayEachHour(country, city);
        ForecastWeatherDto weatherDto = feignClient.getWeatherForTodayEachHour(
                List.of(country, city),
                API_KEY);
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
