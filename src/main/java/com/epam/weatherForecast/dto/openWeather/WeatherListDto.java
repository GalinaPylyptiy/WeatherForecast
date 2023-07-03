package com.epam.weatherForecast.dto.openWeather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Collection;

@Setter
@Getter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherListDto {

    private Collection<WeatherDto> list;
}
