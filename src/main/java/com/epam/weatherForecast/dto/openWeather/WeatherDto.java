package com.epam.weatherForecast.dto.openWeather;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Collection;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherDto {

   private Main main;
   private Collection<Weather> weather;
   private Wind wind;
   @JsonProperty(value = "dt_txt")
   private String dateAndTime;

}
