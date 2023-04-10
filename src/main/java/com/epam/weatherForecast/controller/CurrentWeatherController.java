package com.epam.weatherForecast.controller;

import com.epam.weatherForecast.model.Weather;
import com.epam.weatherForecast.service.WeatherService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/weather")
public class CurrentWeatherController {

    private final WeatherService weatherService;

    public CurrentWeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

        @GetMapping("/current")
    public Weather getCurrentWeather(@RequestParam("city") String city, @RequestParam("country") String country){
        return weatherService.getCurrentWeather(city,country);
    }
}
