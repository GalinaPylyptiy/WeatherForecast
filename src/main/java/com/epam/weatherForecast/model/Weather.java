package com.epam.weatherForecast.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.io.Serializable;
import java.time.LocalDateTime;

public class Weather implements Serializable {

    private String city;

    private String country;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateAndTime;

    private String temperature;

    private String feelsLike;

    private String windSpeed;

//    TODO change field name (type, definition, condition, state)
    private String description;

    public Weather() {
    }

    public Weather(String city, String country, LocalDateTime dateAndTime, String temperature, String feelsLike, String windSpeed, String description) {
        this.city = city;
        this.country = country;
        this.dateAndTime = dateAndTime;
        this.temperature = temperature;
        this.feelsLike = feelsLike;
        this.windSpeed = windSpeed;
        this.description = description;
    }

    public Weather cloneWeatherWithoutDateAndTime(){
        Weather cloned = new Weather();
        cloned.setCity(this.getCity());
        cloned.setCountry(this.getCountry());
        cloned.setTemperature(this.getTemperature());
        cloned.setFeelsLike(this.getFeelsLike());
        cloned.setWindSpeed(this.getWindSpeed());
        cloned.setDescription(this.getDescription());
        return cloned;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getFeelsLike() {
        return feelsLike;
    }

    public void setFeelsLike(String feelsLike) {
        this.feelsLike = feelsLike;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(LocalDateTime dateAndTime) {
        this.dateAndTime = dateAndTime;
    }
}
