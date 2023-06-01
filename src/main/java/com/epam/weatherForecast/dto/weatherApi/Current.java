package com.epam.weatherForecast.dto.weatherApi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Current {

    @JsonProperty(value = "temp_c")
    private double temperature;
    @JsonProperty(value = "feelslike_c")
    private double feelsLike;
    @JsonProperty(value = "wind_mph")
    private double windSpeed;
    private Condition condition;

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getFeelsLike() {
        return feelsLike;
    }

    public void setFeelsLike(double feelsLike) {
        this.feelsLike = feelsLike;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    @Override
    public String toString() {
        return "Current{" +
                "temperature=" + temperature +
                ", feelsLike=" + feelsLike +
                ", windSpeed=" + windSpeed +
                ", condition=" + condition +
                '}';
    }
}
