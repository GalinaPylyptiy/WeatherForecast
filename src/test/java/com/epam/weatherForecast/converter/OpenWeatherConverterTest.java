package com.epam.weatherForecast.converter;

import com.epam.weatherForecast.client.impl.OpenWeatherClientImpl;
import com.epam.weatherForecast.dto.openWeather.Main;
import com.epam.weatherForecast.dto.openWeather.Weather;
import com.epam.weatherForecast.dto.openWeather.WeatherDto;
import com.epam.weatherForecast.dto.openWeather.WeatherListDto;
import com.epam.weatherForecast.dto.openWeather.Wind;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class OpenWeatherConverterTest {

    @Mock
    private OpenWeatherClientImpl openWeatherClient;

    private OpenWeatherConverter converter;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        this.converter = new OpenWeatherConverter(openWeatherClient);
    }

    private WeatherDto getWeatherDto(){
        Main mainWeather = new Main();
        mainWeather.setTemp(13.1);
        mainWeather.setFeelsLike(11.5);
        Weather weather = new Weather();
        weather.setDescription("cloudy");
        Wind wind = new Wind();
        wind.setSpeed(5.5);
        String dateAndTime = "2023-07-14 11:21:30";
       return new WeatherDto(mainWeather, Collections.singleton(weather), wind, dateAndTime);
    }

    private WeatherListDto getWeatherListDto(){
        WeatherListDto weatherListDto = new WeatherListDto();
        weatherListDto.setList(List.of(getWeatherDto()));
        return weatherListDto;
    }

    @Test
    @DisplayName("Should convert WeatherDTO to Weather model and complete all fields in Weather model ")
    void convertCurrentWeatherShouldConvertWeatherFromWeatherDto() {
        String country = "Kazakhstan";
        String city = "Astana";
       when(openWeatherClient.getCurrentWeatherByCountryAndCity(city, country))
           .thenReturn(getWeatherDto());
       WeatherDto weatherDto = openWeatherClient.getCurrentWeatherByCountryAndCity(city, country);
       com.epam.weatherForecast.model.Weather weather = converter.convertCurrentWeather(city, country);
       var expectedValue = String.valueOf(weatherDto.getMain().getTemp());
        assertThat(weather)
                .isNotNull()
                .hasNoNullFieldsOrProperties()
                .extracting(com.epam.weatherForecast.model.Weather::getTemperature)
                .isEqualTo(expectedValue);
    }

    @Test
    void shouldConvertWeatherListDtoToWeatherList() {
        String country = "Kazakhstan";
        String city = "Astana";
        when(openWeatherClient.getWeatherForTodayEach3Hours(country, city)).thenReturn(getWeatherListDto());
        WeatherListDto weatherListDto = openWeatherClient.getWeatherForTodayEach3Hours(country, city);
        List<com.epam.weatherForecast.model.Weather> weatherList = converter.convertWeatherListForToday(city, country);
        var expectedSize = weatherListDto.getList().size();
        assertThat(weatherList)
            .isNotEmpty()
                .hasSize(expectedSize);
    }
}