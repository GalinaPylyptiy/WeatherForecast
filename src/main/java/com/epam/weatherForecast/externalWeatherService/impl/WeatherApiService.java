package com.epam.weatherForecast.externalWeatherService.impl;
import com.epam.weatherForecast.dto.weatherApi.CurrentWeatherDto;
import com.epam.weatherForecast.dto.weatherApi.ForecastDay;
import com.epam.weatherForecast.dto.weatherApi.ForecastWeatherDto;
import com.epam.weatherForecast.dto.weatherApi.Hour;
import com.epam.weatherForecast.externalWeatherService.ExternalWeatherService;
import com.epam.weatherForecast.feignClient.WeatherApiFeignClient;
import com.epam.weatherForecast.mapper.WeatherApiMapper;
import com.epam.weatherForecast.model.Weather;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@PropertySource("classpath:value.properties")
@Component
public class WeatherApiService implements ExternalWeatherService {

    private final WeatherApiFeignClient feignClient;
    private final WeatherApiMapper weatherApiMapper;
    @Value("${weatherAPIKey}")
    private String API_KEY;


    @Override
    public Weather getCurrentWeather(String country, String city) {
       CurrentWeatherDto currentWeatherDto = feignClient.getCurrentWeatherByCityAndCountry(List.of(city, country), API_KEY);
       return weatherApiMapper.toCurrentWeather(currentWeatherDto, city, country);
    }

    @Override
    public Collection<Weather> getWeatherForToday(String country, String city) {
        ForecastWeatherDto todayWeatherList = feignClient.getWeatherForTodayEachHour(List.of(city, country), API_KEY);
        Collection<Weather> weatherForTodayList = weatherApiMapper.toWeatherForTodayList(getHourWeatherList(todayWeatherList));
        weatherForTodayList.forEach(weather -> {
            weather.setCity(city);
            weather.setCountry(country);
        });
        return weatherForTodayList;
    }

    @Override
    public Weather getTodayWeatherForHour(String country, String city, int hour) {
        ForecastWeatherDto todayWeatherByHour = feignClient.getTodayWeatherByHour(List.of(city, country), API_KEY, hour);
        Weather hourWeather = weatherApiMapper.toHourWeather(getHourInstance(getHourWeatherList(todayWeatherByHour)));
        hourWeather.setCity(city);
        hourWeather.setCountry(country);
        return hourWeather;
    }


    private Collection<Hour> getHourWeatherList(ForecastWeatherDto weatherDto){
        return weatherDto.getForecast().getForecastDay().stream()
                .map(ForecastDay::getHour)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private Hour getHourInstance(Collection<Hour> hourWeatherList){
       return hourWeatherList.stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("The hour weather is currently unavailable"));

    }
}
