package com.epam.weatherForecast.dto.openWeather;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collection;


@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherDto {

   private Main main;
   private Collection<Weather> weather;
   private Wind wind;
   @JsonProperty(value = "dt_txt")
   private String dateAndTime;

    public WeatherDto() {
    }

    public WeatherDto(Main main, Collection<Weather> weather, Wind wind, String dateAndTime) {
        this.main = main;
        this.weather = weather;
        this.wind = wind;
        this.dateAndTime = dateAndTime;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public Collection<Weather> getWeather() {
        return weather;
    }

    public void setWeather(Collection<Weather> weather) {
        this.weather = weather;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public String getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(String dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    @Override
    public String toString() {
        return "WeatherDto{" +
                "main=" + main +
                ", weather=" + weather +
                ", wind=" + wind +
                ", dateAndTime=" + dateAndTime +
                '}';
    }
}
