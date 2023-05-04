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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@PropertySource("classpath:value.properties")
public class WeatherApiClientImpl implements WeatherClient {

    private static final String CURRENT_WEATHER_URL = "http://api.weatherapi.com/v1/current.json?key={apiKey}&q={city},{country}&aqi=no";
    private static final String WEATHER_FOR_HOUR = "http://api.weatherapi.com/v1/forecast.json?key={apiKey}&q={city},{country}&hour={hour}";
    private static final String WEATHER_FOR_TODAY = "http://api.weatherapi.com/v1/forecast.json?key={apiKey}&q={city},{country}&days=1";
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
        URI url = new UriTemplate(CURRENT_WEATHER_URL).expand(API_KEY, city, country);
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return convertCurrent(response, city, country);
    }

    @Override
    public List<Weather> getWeatherForToday(String country, String city) {
        URI url = new UriTemplate(WEATHER_FOR_TODAY).expand(API_KEY,city,country);
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return convertWeatherList(response,city,country);
    }

    @Override
    public Weather getTodayWeatherForHour(String country, String city, int hour) {
        URI url = new UriTemplate(WEATHER_FOR_HOUR).expand(API_KEY, city, country, hour);
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return convertHourWeather(response, city, country);
    }

    private Weather convertCurrent(ResponseEntity<String> response, String city, String country) {
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

    private Weather convertHourWeather(ResponseEntity<String> response, String city, String country) {
        try {
            JsonNode root = objectMapper.readTree(Objects.requireNonNull(response.getBody()));
            JsonNode jsonNodeArray = root.path("forecast").get("forecastday");
            Weather weather = new Weather();
            jsonNodeArray.forEach(jsonNode ->
                    jsonNode.get("hour")
                            .forEach(hourWeather -> {
                                setWeatherData(city, country, weather, hourWeather);
                            }));
            return weather;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error parsing JSON", e);
        }
    }

    private List<Weather> convertWeatherList(ResponseEntity<String> response, String city, String country){
        List<Weather> todayWeatherList = new ArrayList<>();
      try{
          JsonNode root = objectMapper.readTree(Objects.requireNonNull(response.getBody()));
          JsonNode jsonNodeArray = root.path("forecast").get("forecastday");
          jsonNodeArray.forEach(dayWeatherArray -> {
              dayWeatherArray.get("hour")
                      .forEach(hourWeather->{
                          Weather weather = new Weather();
                          setWeatherData(city, country, weather, hourWeather);
                          todayWeatherList.add(weather);
                      }
              );
          } );
      } catch (JsonProcessingException e) {
          throw new RuntimeException("Error parsing JSON", e);
      }
      return todayWeatherList;
    }

    private void setWeatherData(String city, String country, Weather weather, JsonNode hourWeather) {
        weather.setCity(city);
        weather.setCountry(country);
        weather.setTemperature(hourWeather.path("temp_c").asText());
        weather.setFeelsLike(hourWeather.path("feelslike_c").asText());
        weather.setWindSpeed(hourWeather.path("wind_mph").asText());
        weather.setDescription(hourWeather.path("condition").path("text").asText());
        weather.setDateAndTime(LocalDateTime.parse(hourWeather.path("time").asText(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
    }
}
