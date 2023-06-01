package com.epam.weatherForecast.dto.openWeather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Collection;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherListDto {

    private Collection<WeatherDto> list;

    public Collection<WeatherDto> getList() {
        return list;
    }

    public void setList(Collection<WeatherDto> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "WeatherListDto{" +
                "list=" + list +
                '}';
    }
}
