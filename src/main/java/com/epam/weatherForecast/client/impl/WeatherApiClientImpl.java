package com.epam.weatherForecast.client.impl;
import com.epam.weatherForecast.client.WeatherClient;
import com.epam.weatherForecast.dto.weatherApi.CurrentWeatherDto;
import com.epam.weatherForecast.dto.weatherApi.ForecastWeatherDto;
import com.epam.weatherForecast.dto.weatherApi.ForecastDay;
import com.epam.weatherForecast.dto.weatherApi.Hour;
import com.epam.weatherForecast.model.Weather;
import com.fasterxml.jackson.core.JsonProcessingException;
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
import java.util.Collection;
import java.util.Objects;

@Component
@PropertySource("classpath:value.properties")
public class WeatherApiClientImpl implements WeatherClient {

    private static final String CURRENT_WEATHER_URL = "http://api.weatherapi.com/v1/current.json?key={apiKey}&q={city},{country}&aqi=no";
    private static final String WEATHER_FOR_HOUR = "http://api.weatherapi.com/v1/forecast.json?key={apiKey}&q={city},{country}&hour={hour}";
    private static final String WEATHER_FOR_TODAY = "http://api.weatherapi.com/v1/forecast.json?key={apiKey}&q={city},{country}&days=1";
    private static final String ERROR_MSG = "Error parsing JSON";
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
    public Collection<Weather> getWeatherForToday(String country, String city) {
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
            CurrentWeatherDto currentWeatherDto = objectMapper.readValue(Objects.requireNonNull(response.getBody()), CurrentWeatherDto.class);
            Weather weather = new Weather();
            weather.setTemperature(String.valueOf(currentWeatherDto.getCurrent().getTemperature()));
            weather.setFeelsLike(String.valueOf(currentWeatherDto.getCurrent().getFeelsLike()));
            weather.setDescription(currentWeatherDto.getCurrent().getCondition().getDescription());
            weather.setWindSpeed(String.valueOf(currentWeatherDto.getCurrent().getWindSpeed()));
            weather.setCity(city);
            weather.setCountry(country);
            weather.setDateAndTime(LocalDateTime.now());
            return weather;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(ERROR_MSG, e);
        }
    }

    private Weather convertHourWeather(ResponseEntity<String> response, String city, String country) {
        try {
            ForecastWeatherDto weatherDto = objectMapper.readValue(Objects.requireNonNull(response.getBody()), ForecastWeatherDto.class);
            Weather weather = new Weather();
            for(ForecastDay forecastday: weatherDto.getForecast().getForecastday()){
                forecastday.getHour().forEach(hourWeather-> setWeatherData(city, country, hourWeather, weather));
            }
            return weather;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(ERROR_MSG, e);
        }
    }

    private Collection<Weather> convertWeatherList(ResponseEntity<String> response, String city, String country){
        Collection<Weather> todayWeatherList = new ArrayList<>();
      try{
          ForecastWeatherDto weatherDto = objectMapper.readValue(Objects.requireNonNull(response.getBody()), ForecastWeatherDto.class);
          for(ForecastDay forecastday: weatherDto.getForecast().getForecastday()){
              forecastday.getHour().forEach(hourWeather->{
                  Weather weather = new Weather();
                  setWeatherData(city, country, hourWeather, weather);
                  todayWeatherList.add(weather);
              });
          }
      } catch (JsonProcessingException e) {
          throw new RuntimeException(ERROR_MSG, e);
      }
      return todayWeatherList;
    }

    private void setWeatherData(String city, String country, Hour hourWeather, Weather weather) {
        String dateAndTimePattern = "yyyy-MM-dd HH:mm";
        weather.setCity(city);
        weather.setCountry(country);
        weather.setTemperature(String.valueOf(hourWeather.getTemperature()));
        weather.setFeelsLike(String.valueOf(hourWeather.getFeelsLike()));
        weather.setWindSpeed(String.valueOf(hourWeather.getWindSpeed()));
        weather.setDescription(hourWeather.getCondition().getDescription());
        weather.setDateAndTime(LocalDateTime.parse(hourWeather.getDateAndTime(),
                DateTimeFormatter.ofPattern(dateAndTimePattern)));
    }
}
