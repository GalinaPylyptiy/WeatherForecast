package com.epam.weatherForecast.service.impl;

import com.epam.weatherForecast.client.impl.OpenWeatherClientImpl;
import com.epam.weatherForecast.model.Weather;
import com.epam.weatherForecast.client.impl.WeatherApiClientImpl;
import com.epam.weatherForecast.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;


@Service
public class WeatherServiceImpl implements WeatherService {

    private final OpenWeatherClientImpl openWeatherClient;
    private final WeatherApiClientImpl weatherApiClient;

    @Autowired
    public WeatherServiceImpl(OpenWeatherClientImpl openWeatherClient, WeatherApiClientImpl weatherApiClient) {
        this.openWeatherClient = openWeatherClient;
        this.weatherApiClient = weatherApiClient;
    }

    @Override
    public Weather getCurrentWeather(String city, String country) {
        Weather weather1 = openWeatherClient.getCurrentWeather(city, country);
        Weather weather2 = weatherApiClient.getCurrentWeather(city, country);
        LocalDateTime now = LocalDateTime.now();
        String  averageTemp = String.valueOf(calculateAverageTemperature(weather1, weather2));
        String averageFeelsLike =String.valueOf(calculateAverageFeelsLikeTemp(weather1, weather2));
        String averageWindSpeed =String.valueOf(calculateAverageWindSpeed(weather1, weather2));
        String description = getDescription(weatherApiClient, city, country);
        return new Weather(city, country, now, averageTemp,
                averageFeelsLike, averageWindSpeed, description);
    }

    private int calculateAverageTemperature(Weather weather1, Weather weather2) {
        int temp1  = convert(weather1.getTemperature());
        int temp2  = convert(weather2.getTemperature());
        return (temp1+temp2)/2;
    }

    private int calculateAverageWindSpeed(Weather weather1, Weather weather2) {
        int wind1  = convert(weather1.getWindSpeed());
        int wind2  = convert(weather2.getWindSpeed());
        return (wind1+wind2)/2;
    }

    private int calculateAverageFeelsLikeTemp(Weather weather1, Weather weather2) {
        int feelsLike1  = convert(weather1.getFeelsLike());
        int feelsLike2  = convert(weather2.getFeelsLike());
        return (feelsLike1+feelsLike2)/2;
    }

    private String getDescription(WeatherApiClientImpl weatherApiClient,String city, String country){
        return weatherApiClient.getCurrentWeather(city, country).getDescription();
    }

    private int convert (String value ){
      double result = Double.parseDouble(value);
      return (int) result;
    }

}
