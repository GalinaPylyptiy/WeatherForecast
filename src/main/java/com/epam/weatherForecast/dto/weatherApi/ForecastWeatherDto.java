package com.epam.weatherForecast.dto.weatherApi;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class ForecastWeatherDto {

    private Forecast forecast;

}
