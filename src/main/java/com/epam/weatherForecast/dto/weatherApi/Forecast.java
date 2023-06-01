package com.epam.weatherForecast.dto.weatherApi;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Collection;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Forecast {

    private Collection<ForecastDay> forecastday;

    public Collection<ForecastDay> getForecastday() {
        return forecastday;
    }

    public void setForecastDay(Collection<ForecastDay> forecastday) {
        this.forecastday = forecastday;
    }

    @Override
    public String toString() {
        return "Forecast{" +
                "forecastDay=" + forecastday +
                '}';
    }
}
