package com.epam.weatherForecast.client.impl;

import com.epam.weatherForecast.client.WeatherClient;
import com.epam.weatherForecast.model.Weather;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Objects;

@Component
@PropertySource("classpath:value.properties")
public class OpenWeatherClientImpl implements WeatherClient {

    private static final String WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather?q={city},{country}&appid={key}&units=metric&lang=ru";
    @Value("${openWeatherApiKey}")
    private String apiKey;
    private  RestTemplate restTemplate;
    private  ObjectMapper objectMapper;

    @Autowired
    public OpenWeatherClientImpl(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public Weather getCurrentWeather(String city, String country) {
        URI url = new UriTemplate(WEATHER_URL).expand(city, country, apiKey);
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return convert(response, city, country);
    }

    private Weather convert(ResponseEntity<String> response,String city, String country) {
        try {
            JsonNode root = objectMapper.readTree(Objects.requireNonNull(response.getBody()));
            Weather weather = new Weather();
            weather.setCity(city);
            weather.setCountry(country);
            weather.setTemperature(root.path("main").path("temp").asText());
            weather.setFeelsLike(root.path("main").path("feels_like").asText());
            weather.setWindSpeed(root.path("wind").path("speed").asText());
            weather.setDateAndTime(LocalDateTime.now());
            return weather;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error parsing JSON", e);
        }
    }

}
