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
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@PropertySource("classpath:value.properties")
public class OpenWeatherClientImpl implements WeatherClient {

    private static final String CURRENT_WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather?q={city},{country}&appid={key}&units=metric&lang=ru";
    private static final String TODAY_WEATHER_URL = "http://api.openweathermap.org/data/2.5/forecast?q={city},{country}&appid={key}&units=metric&cnt=6";
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
        URI url = new UriTemplate(CURRENT_WEATHER_URL).expand(city, country, apiKey);
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return convert(response, city, country);
    }

    @Override
    public List<Weather> getWeatherForToday(String country, String city) {
        URI url = new UriTemplate(TODAY_WEATHER_URL).expand(city, country, apiKey);
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return (convertList(response, city, country));
    }

    @Override
    public Weather getTodayWeatherForHour(String country, String city, int hour) {
        LocalTime time = LocalTime.of(hour, 0,0);
        List<Weather> todayWeather = getWeatherForToday(country,city);
        Optional<Weather> weatherForHour = todayWeather.stream().filter(unit ->
                    unit.getDateAndTime().toLocalTime().
                            isAfter(time)).findAny();
        return weatherForHour.orElse(getCurrentWeather(city, country));
    }

    private Weather convert(ResponseEntity<String> response,String city, String country) {
        try {
            JsonNode root = objectMapper.readTree(Objects.requireNonNull(response.getBody()));
            Weather weather = new Weather();
            setWeatherData(city, country, root,weather);
            weather.setDateAndTime(LocalDateTime.now());
            return weather;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error parsing JSON", e);
        }
    }

    private List<Weather> convertList(ResponseEntity<String> response, String city, String country) {
        List<Weather> todayWeatherList = new ArrayList<>();
        try {
            JsonNode root = objectMapper.readTree(Objects.requireNonNull(response.getBody()));
            JsonNode jsonObjectArray = root.get("list");
            for (JsonNode jsonNode : jsonObjectArray) {
                Weather weather = new Weather();
                setWeatherData(city, country, jsonNode, weather);
                JsonNode weatherArray = jsonNode.get("weather");
                weatherArray.forEach(desc ->
                        weather.setDescription(desc.path("description").asText()));
                weather.setDateAndTime(LocalDateTime.parse(jsonNode.path("dt_txt").asText(),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                todayWeatherList.add(weather);
            }
            return todayWeatherList;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error parsing JSON", e);
        }
    }

//    TODO DTO

    private void setWeatherData(String city, String country, JsonNode jsonNode,Weather weather) {
        weather.setCity(city);
        weather.setCountry(country);
        weather.setTemperature(jsonNode.path("main").path("temp").asText());
        weather.setFeelsLike(jsonNode.path("main").path("feels_like").asText());
        weather.setWindSpeed(jsonNode.path("wind").path("speed").asText());
    }

}
