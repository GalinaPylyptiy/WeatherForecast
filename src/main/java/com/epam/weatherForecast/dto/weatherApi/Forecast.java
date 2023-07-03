package com.epam.weatherForecast.dto.weatherApi;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Collection;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Forecast {

    @JsonProperty(value = "forecastday")
    private Collection<ForecastDay> forecastDay;

}
