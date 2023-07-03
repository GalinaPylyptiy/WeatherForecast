package com.epam.weatherForecast.client.impl;

import com.epam.weatherForecast.client.WeatherApiClient;
import com.epam.weatherForecast.dto.weatherApi.CurrentWeatherDto;
import com.epam.weatherForecast.dto.weatherApi.ForecastWeatherDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;

import java.net.URI;

@Component
@PropertySource("classpath:value.properties")
public class WeatherApiClientImpl implements WeatherApiClient {

    private static final String CURRENT_WEATHER_URL = "http://api.weatherapi.com/v1/current.json?key={apiKey}&q={city},{country}&aqi=no";
    private static final String WEATHER_FOR_HOUR = "http://api.weatherapi.com/v1/forecast.json?key={apiKey}&q={city},{country}&hour={hour}";
    private static final String WEATHER_FOR_TODAY = "http://api.weatherapi.com/v1/forecast.json?key={apiKey}&q={city},{country}&days=1";
    @Value("${weatherAPIKey}")
    private String API_KEY;
    private final RestTemplate restTemplate;

    public WeatherApiClientImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public CurrentWeatherDto getCurrentWeatherByCityAndCountry(String country, String city) {

        URI url = new UriTemplate(CURRENT_WEATHER_URL).expand(API_KEY, city, country);
        return restTemplate.getForObject(url, CurrentWeatherDto.class);
    }

    public ForecastWeatherDto getWeatherForTodayEachHour(String country, String city) {
        URI url = new UriTemplate(WEATHER_FOR_TODAY).expand(API_KEY, city, country);
        return restTemplate.getForObject(url, ForecastWeatherDto.class);
    }

    public ForecastWeatherDto getTodayWeatherByHour(String country, String city, int hour) {
        URI url = new UriTemplate(WEATHER_FOR_HOUR).expand(API_KEY, city, country, hour);
        return restTemplate.getForObject(url, ForecastWeatherDto.class);
    }

}
