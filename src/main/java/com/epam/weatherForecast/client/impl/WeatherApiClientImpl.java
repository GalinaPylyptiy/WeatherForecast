package com.epam.weatherForecast.client.impl;

import com.epam.weatherForecast.client.WeatherApiClient;
import com.epam.weatherForecast.dto.weatherApi.CurrentWeatherDto;
import com.epam.weatherForecast.dto.weatherApi.ForecastWeatherDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;

import java.net.URI;
import java.util.Objects;

@Component
@PropertySource("classpath:value.properties")
public class WeatherApiClientImpl implements WeatherApiClient {

    private static final String CURRENT_WEATHER_URL = "http://api.weatherapi.com/v1/current.json?key={apiKey}&q={city},{country}&aqi=no";
    private static final String WEATHER_FOR_HOUR = "http://api.weatherapi.com/v1/forecast.json?key={apiKey}&q={city},{country}&hour={hour}";
    private static final String WEATHER_FOR_TODAY = "http://api.weatherapi.com/v1/forecast.json?key={apiKey}&q={city},{country}&days=1";
    private static final String ERROR_MSG = "Error parsing json";
    @Value("${weatherAPIKey}")
    private String API_KEY;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public WeatherApiClientImpl(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public CurrentWeatherDto getCurrentWeatherByCityAndCountry(String country, String city) {
        try {
            ResponseEntity<String> response = getResponse(CURRENT_WEATHER_URL, country, city);
            return objectMapper.readValue(Objects.requireNonNull(response.getBody()), CurrentWeatherDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(ERROR_MSG, e);
        }
    }

    public ForecastWeatherDto getWeatherForTodayEachHour(String country, String city) {
        try {
            ResponseEntity<String> response = getResponse(WEATHER_FOR_TODAY, country, city);
            return objectMapper.readValue(Objects.requireNonNull(response.getBody()), ForecastWeatherDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(ERROR_MSG, e);
        }
    }

    public ForecastWeatherDto getTodayWeatherByHour(String country, String city, int hour) {
        try {
            URI url = new UriTemplate(WEATHER_FOR_HOUR).expand(API_KEY, city, country, hour);
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            return objectMapper.readValue(Objects.requireNonNull(response.getBody()), ForecastWeatherDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(ERROR_MSG, e);
        }
    }

    private ResponseEntity<String> getResponse(String stringUrl, String country, String city) {
        URI url = new UriTemplate(stringUrl).expand(API_KEY, city, country);
        return restTemplate.getForEntity(url, String.class);
    }

}
