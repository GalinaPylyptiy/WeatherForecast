package com.epam.weatherForecast.dto.weatherApi;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ForecastWeatherDto {

    private Forecast forecast;

    public Forecast getForecast() {
        return forecast;
    }

    public void setForecast(Forecast forecast) {
        this.forecast = forecast;
    }

    @Override
    public String toString() {
        return "ForecastWeatherDto{" +
                "forecast=" + forecast +
                '}';
    }
}
