package com.epam.weatherForecast.controller;

import com.epam.weatherForecast.model.Weather;
import com.epam.weatherForecast.service.WeatherService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.Collection;

@RestController
@RequestMapping("/weather")
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

        @GetMapping("/current")
    public Weather getCurrentWeather(@RequestParam("city") String city,
                                     @RequestParam("country") String country){
        return weatherService.getCurrentWeather(city,country);
    }

    @GetMapping("/today")
    public Collection<Weather> getWeatherForToday(@RequestParam("city") String city,
                                                  @RequestParam("country") String country,
                                                  @RequestParam("eachHour") boolean eachHour){
        return weatherService.getWeatherForToday(country, city, eachHour);
    }

    @GetMapping("/hour")
    public Weather getWeatherPerHour(@RequestParam("city") String city,
                                     @RequestParam("country") String country,
                                     @RequestParam("hour") String hour){
        return weatherService.getTodayWeatherForHour(country, city, Integer.parseInt(hour));
    }

    @GetMapping("/compare")
    public String compareTwoCitiesTemp(@RequestParam("city1") String city1,
                                       @RequestParam("city2") String city2,
                                       @RequestParam("country") String country ){
        return weatherService.compareTwoCitiesTemperature(city1, city2, country);
    }

}
