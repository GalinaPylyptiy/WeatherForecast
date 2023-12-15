package com.epam.weatherForecast.dto.weatherApi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Hour {

    @JsonProperty(value = "time")
    private String dateAndTime;
    @JsonProperty(value = "temp_c")
    private double temperature;
    @JsonProperty(value = "feelslike_c")
    private double feelsLike;
    @JsonProperty(value = "wind_mph")
    private double windSpeed;
    private Condition condition;

}
