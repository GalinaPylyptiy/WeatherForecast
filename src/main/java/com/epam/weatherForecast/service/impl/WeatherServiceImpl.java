package com.epam.weatherForecast.service.impl;

import com.epam.weatherForecast.client.impl.OpenWeatherClientImpl;
import com.epam.weatherForecast.model.Weather;
import com.epam.weatherForecast.client.impl.WeatherApiClientImpl;
import com.epam.weatherForecast.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


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
        String averageTemp = String.valueOf(calculateAverageTemperature(weather1, weather2));
        String averageFeelsLike = String.valueOf(calculateAverageFeelsLikeTemp(weather1, weather2));
        String averageWindSpeed = String.valueOf(calculateAverageWindSpeed(weather1, weather2));
        String description = weather2.getDescription();
        return new Weather(city, country, now, averageTemp,
                averageFeelsLike, averageWindSpeed, description);
    }

    @Override
    public List<Weather> getWeatherForToday(String country, String city, boolean eachHour) {
        if (eachHour){
         return weatherApiClient.getWeatherForToday(country, city);
        }
          return openWeatherClient.getWeatherForToday(country,city);
    }

    @Override
    public Weather getTodayWeatherForHour(String country, String city, int hour) {
        if (hour < 0 || hour >= 24) {
            throw new IllegalArgumentException("Inserted hour should be from  0 to 23");
        }
        Weather weather1 = weatherApiClient.getTodayWeatherForHour(country, city, hour);
        Weather weather2 = openWeatherClient.getTodayWeatherForHour(country, city, hour);
        Weather resultWeather = new Weather();
        resultWeather.setDateAndTime(weather1.getDateAndTime());
        resultWeather.setTemperature(String.valueOf(calculateAverageTemperature(weather1, weather2)));
        resultWeather.setFeelsLike(String.valueOf(calculateAverageFeelsLikeTemp(weather1, weather2)));
        resultWeather.setWindSpeed(String.valueOf(calculateAverageWindSpeed(weather1, weather2)));
        resultWeather.setDescription(weather1.getDescription());
        resultWeather.setCountry(country);
        resultWeather.setCity(city);
        return resultWeather;
    }

    @Override
    public String compareTwoCitiesTemperature(String city1, String city2, String country) {
        Weather weatherCity1 = getCurrentWeather(city1, country);
        Weather weatherCity2 = getCurrentWeather(city2, country);
        int temp1 = Integer.parseInt(weatherCity1.getTemperature());
        int temp2 = Integer.parseInt(weatherCity2.getTemperature());
        if (temp1 > temp2)
            return "Weather in " + city1 + " is higher on " + (temp1 - temp2) + "°C";
        return "Weather in " + city2 + " is higher on " + (temp2 - temp1) + "°C";
    }

    private int calculateAverageTemperature(Weather weather1, Weather weather2) {
        int temp1 = convert(weather1.getTemperature());
        int temp2 = convert(weather2.getTemperature());
        return (temp1 + temp2) / 2;
    }

    private int calculateAverageWindSpeed(Weather weather1, Weather weather2) {
        int wind1 = convert(weather1.getWindSpeed());
        int wind2 = convert(weather2.getWindSpeed());
        return (wind1 + wind2) / 2;
    }

    private int calculateAverageFeelsLikeTemp(Weather weather1, Weather weather2) {
        int feelsLike1 = convert(weather1.getFeelsLike());
        int feelsLike2 = convert(weather2.getFeelsLike());
        return (feelsLike1 + feelsLike2) / 2;
    }

    private int convert(String value) {
        double result = Double.parseDouble(value);
        return (int) result;
    }

//    TODO Collection<WeatherClient>

}
