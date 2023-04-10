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
public class WeatherApiClientImpl implements WeatherClient {

    private static final String WEATHER_URL = "http://api.weatherapi.com/v1/current.json?key={apiKey}&q={city},{country}&aqi=no";
    @Value("${weatherAPIKey}")
    private String API_KEY;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public WeatherApiClientImpl(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public Weather getCurrentWeather(String country, String city) {
        URI url = new UriTemplate(WEATHER_URL).expand(API_KEY,city,country);
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return convert(response, city, country);
    }

    private Weather convert(ResponseEntity<String> response, String city, String country) {
        try {
            JsonNode root = objectMapper.readTree(Objects.requireNonNull(response.getBody()));
            Weather weather = new Weather();
            weather.setCity(city);
            weather.setCountry(country);
            weather.setTemperature(root.path("current").path("temp_c").asText());
            weather.setFeelsLike(root.path("current").path("feelslike_c").asText());
            weather.setWindSpeed(root.path("current").path("wind_mph").asText());
            weather.setDescription(root.path("current").path("condition").path("text").asText());
            weather.setDateAndTime(LocalDateTime.now());
            return weather;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error parsing JSON", e);
        }
    }
}
