package com.epam.weatherForecast.dto.weatherApi;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collection;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Forecast {

    @JsonProperty(value = "forecastday")
    private Collection<ForecastDay> forecastDay;

    public Collection<ForecastDay> getForecastDay() {
        return forecastDay;
    }

    public void setForecastDay(Collection<ForecastDay> forecastDay) {
        this.forecastDay = forecastDay;
    }

    @Override
    public String toString() {
        return "Forecast{" +
                "forecastDay=" + forecastDay +
                '}';
    }
}
