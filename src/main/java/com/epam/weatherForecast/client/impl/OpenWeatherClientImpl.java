package com.epam.weatherForecast.client.impl;

import com.epam.weatherForecast.client.OpenWeatherClient;
import com.epam.weatherForecast.dto.openWeather.WeatherDto;
import com.epam.weatherForecast.dto.openWeather.WeatherListDto;
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
public class OpenWeatherClientImpl implements OpenWeatherClient {

    private static final String CURRENT_WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather?q={city},{country}&appid={key}&units=metric";
    private static final String TODAY_WEATHER_URL = "http://api.openweathermap.org/data/2.5/forecast?q={city},{country}&appid={key}&units=metric&cnt=8";
    private static final String ERROR_MSG = "Error parsing json";
    @Value("${openWeatherApiKey}")
    private String apiKey;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public OpenWeatherClientImpl(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public WeatherDto getCurrentWeatherByCountryAndCity(String city, String country) {
        try {
            ResponseEntity<String> response = getResponse(CURRENT_WEATHER_URL, country, city);
            return objectMapper.readValue(Objects.requireNonNull(response.getBody()), WeatherDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(ERROR_MSG, e);
        }
    }

    public WeatherListDto getWeatherForTodayEach3Hours(String country, String city) {
        try {
            ResponseEntity<String> response = getResponse(TODAY_WEATHER_URL, country, city);
            return objectMapper.readValue(Objects.requireNonNull(response.getBody()), WeatherListDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(ERROR_MSG, e);
        }
    }

    private ResponseEntity<String> getResponse(String stringUrl, String country, String city) {
        URI url = new UriTemplate(stringUrl).expand(city, country, apiKey);
        return restTemplate.getForEntity(url, String.class);
    }

}
