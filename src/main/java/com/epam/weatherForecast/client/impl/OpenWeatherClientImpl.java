package com.epam.weatherForecast.client.impl;
import com.epam.weatherForecast.client.WeatherClient;
import com.epam.weatherForecast.dto.openWeather.WeatherDto;
import com.epam.weatherForecast.dto.openWeather.WeatherListDto;
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
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

@Component
@PropertySource("classpath:value.properties")
public class OpenWeatherClientImpl implements WeatherClient {

    private static final String CURRENT_WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather?q={city},{country}&appid={key}&units=metric&lang=ru";
    private static final String TODAY_WEATHER_URL = "http://api.openweathermap.org/data/2.5/forecast?q={city},{country}&appid={key}&units=metric&cnt=6";
    private static final String ERROR_MSG = "Error parsing JSON";
    @Value("${openWeatherApiKey}")
    private String apiKey;
    private RestTemplate restTemplate;
    private ObjectMapper objectMapper;

    @Autowired
    public OpenWeatherClientImpl(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public Weather getCurrentWeather(String city, String country) {
        URI url = new UriTemplate(CURRENT_WEATHER_URL).expand(city, country, apiKey);
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return convert(response,city, country);
    }

        @Override
        public Collection<Weather> getWeatherForToday (String country, String city){
            URI url = new UriTemplate(TODAY_WEATHER_URL).expand(city, country, apiKey);
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            return (convertList(response, city, country));
        }

        @Override
        public Weather getTodayWeatherForHour (String country, String city,int hour){
            LocalTime time = LocalTime.of(hour, 0, 0);
            Collection<Weather> todayWeather = getWeatherForToday(country, city);
            Optional<Weather> weatherForHour = todayWeather.stream().filter(unit ->
                    unit.getDateAndTime().toLocalTime().
                            isAfter(time)).findAny();
            return weatherForHour.orElse(getCurrentWeather(city, country));
        }

        private Weather convert (ResponseEntity <String> response,String city, String country){
            try {
                WeatherDto weatherDto = objectMapper.readValue(Objects.requireNonNull(response.getBody()), WeatherDto.class);
                Weather weather = new Weather();
                setWeatherData(city, country, weatherDto, weather);
                weather.setDateAndTime(LocalDateTime.now());

                return weather;
            } catch (JsonProcessingException e) {
                throw new RuntimeException(ERROR_MSG, e);
            }
        }

        private Collection<Weather> convertList (ResponseEntity < String > response, String city, String country){
            Collection<Weather> todayWeatherList = new ArrayList<>();
            String dateAndTimePattern = "yyyy-MM-dd HH:mm:ss";
            try {
                WeatherListDto weatherListDto = objectMapper.readValue(Objects.requireNonNull(response.getBody()),WeatherListDto.class);
                for(WeatherDto weatherDto: weatherListDto.getList()){
                    Weather weather = new Weather();
                    setWeatherData(city, country, weatherDto, weather);
                    weather.setDateAndTime(LocalDateTime.parse(weatherDto.getDateAndTime(),
                            DateTimeFormatter.ofPattern(dateAndTimePattern)));
                    todayWeatherList.add(weather);
                }
            } catch (JsonProcessingException e) {
                throw new RuntimeException(ERROR_MSG, e);
            }
            return todayWeatherList;
        }

        private void setWeatherData(String city, String country,WeatherDto weatherDto, Weather weather){
            weather.setCity(city);
            weather.setCountry(country);
            weather.setTemperature(String.valueOf(weatherDto.getMain().getTemp()));
            weather.setFeelsLike(String.valueOf(weatherDto.getMain().getFeelsLike()));
            weather.setWindSpeed(String.valueOf(weatherDto.getWind().getSpeed()));
            weatherDto.getWeather().forEach(desc -> weather.setDescription(desc.getDescription()));
        }
    }

