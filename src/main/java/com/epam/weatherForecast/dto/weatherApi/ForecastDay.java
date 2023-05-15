package com.epam.weatherForecast.dto.weatherApi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Collection;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ForecastDay {

    private Collection<Hour> hour;

    public Collection<Hour> getHour() {
        return hour;
    }

    public void setHour(Collection<Hour> hour) {
        this.hour = hour;
    }

    @Override
    public String toString() {
        return "Forecastday{" +
                "hour=" + hour +
                '}';
    }
}
