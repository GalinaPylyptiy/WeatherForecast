package com.epam.weatherForecast.util.impl;

import com.epam.weatherForecast.model.Weather;
import com.epam.weatherForecast.util.AverageValueCalculator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class AverageValueCalculatorImplTest {

    private AverageValueCalculator calculator = new AverageValueCalculatorImpl();

    private List<Weather> getWeatherList() {
        Weather weather1 = new Weather("London", "Great Britain", LocalDateTime.now(),
            "-4", "-11", "5", "snowy");
        Weather weather2 = new Weather("London", "Great Britain", LocalDateTime.now(),
            "-7", "-10", "7", "freezing");
        return List.of(weather1, weather2);
    }

    //todo parameters test with argument capture, with list of different size @MethodSource
    @ParameterizedTest
    @DisplayName("Must calculate average value in temperature")
    @CsvSource(value = {
        "-4  , -6  , -5 ",
        "4   , 6   , 5  ",
        "10  , 20  , 15 ",
        "0   , 1   , 1  ",  //TODO test isn't work
        "0   , -1  , 0  "
    })
    void shouldCalculateAverageTemperature(String value1, String value2, int expectedValue) {
        //given
        List<String> temperatures = List.of(value1, value2);
        var testWeatherList = createWeatherListFromTemperatures(temperatures);

        //when
        int averageTemp = calculator.calculateAverageValue(testWeatherList, Weather::getTemperature);

        //then
        assertThat(averageTemp)
            .isEqualTo(expectedValue);
    }

    @ParameterizedTest
    @DisplayName("Must calculate average value in negative temperature")
    @CsvSource(value = {
        "-4 , -7",
        "-50 , -6",
        "-100 , -7",
        "200 , -700",
        "-2 , 0",
    })
    void shouldCalculateAverageNegativeTemperature(String value1, String value2) {
        //given
        List<String> temperatures = List.of(value1, value2);
        var testWeatherList = createWeatherListFromTemperatures(temperatures);

        //when
        int averageTemp = calculator.calculateAverageValue(testWeatherList, Weather::getTemperature);

        //then
        assertThat(averageTemp)
            .isNegative();
    }

    private List<Weather> createWeatherListFromTemperatures(List<String> temperatures) {
        return temperatures.stream()
            .map(temperature ->
                new Weather("London", "Great Britain", LocalDateTime.now(),
                    temperature, "-10", "7", "freezing")
            )
            .collect(Collectors.toList());
    }

    @Test
    void shouldCalculateAverageTemperatureAndFeelsLikeAndReturnNegativeResult() {
        int averageTemp = calculator.calculateAverageValue(getWeatherList(), Weather::getTemperature);
        int averageFeelsLike = calculator.calculateAverageValue(getWeatherList(), Weather::getFeelsLike);
        assertThat(averageTemp)
            .isNegative();
        assertThat(averageFeelsLike)
            .isNegative();
    }

    @Test
    void shouldCalculateAverageWindSpeed() {
        int averageWindSpeed = calculator.calculateAverageValue(getWeatherList(), Weather::getWindSpeed);
        int result = 6;
        assertThat(averageWindSpeed).isEqualTo(result);
    }
}