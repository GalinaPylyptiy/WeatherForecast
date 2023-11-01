package com.epam.weatherForecast.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Weather implements Serializable{

    private String city;

    private String country;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime dateAndTime;

    private String temperature;

    private String feelsLike;

    private String windSpeed;

//    TODO change field name (type, definition, condition, state)
    private String description;

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

}
