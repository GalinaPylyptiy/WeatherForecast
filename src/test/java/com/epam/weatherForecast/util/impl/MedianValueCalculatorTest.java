package com.epam.weatherForecast.util.impl;

import com.epam.weatherForecast.model.Weather;
import com.epam.weatherForecast.util.AverageValueCalculator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static org.assertj.core.api.Assertions.assertThat;

class MedianValueCalculatorTest {

    private AverageValueCalculator calculator = new MedianValueCalculator();

    @ParameterizedTest
    @MethodSource("generateOddListAndExpectedValue")
    @DisplayName("Must calculate median temperature from odd sized list of temperature")
    void shouldCalculateMedianTemperatureFromGivenOddSizedListOfWeather(List<String> temperatures, int expectedValue) {

        //given
        List<Weather> weatherList = createWeatherListFromTemperature(temperatures);

        //when
        int result = calculator.calculateAverageValue(weatherList, Weather::getTemperature);

        //then
        assertThat(result)
                .isEqualTo(expectedValue);
    }

    @ParameterizedTest
    @MethodSource("generateEvenListAndExpectedValue")
    @DisplayName("Must calculate median temperature from even sized list of temperature")
    void shouldCalculateMedianTemperatureFromGivenEvenSizedListOfWeather(List<String> temperatures, int expectedValue) {

        //given
        List<Weather> weatherList = createWeatherListFromTemperature(temperatures);

        //when
        int result = calculator.calculateAverageValue(weatherList, Weather::getTemperature);

        //then
        assertThat(result)
                .isEqualTo(expectedValue);
    }

    private static Stream<Arguments> generateOddListAndExpectedValue(){
       return Stream.of(
                Arguments.of(List.of("15.2", "10.7", "13.9"), 14),
                Arguments.of(List.of("-8.3", "-6.4", "-3.8"), -6),
                Arguments.of(List.of("0", "1.2", "2.4"), 1),
                Arguments.of(List.of("0", "1.7", "-1.2"),0)
        );
    }

    private static Stream<Arguments> generateEvenListAndExpectedValue(){
        return Stream.of(
                Arguments.of(List.of("15.8", "10.6", "13.3", "9.9"), 12),
                Arguments.of(List.of("-8.4", "-6.3", "-3.7", "-7.6"), -7),
                Arguments.of(List.of("0", "1.6", "2.5", "-1.3"), 1),
                Arguments.of(List.of("0", "1.8", "-1.4", "-2.6"),-1)
        );
    }

    private List<Weather> createWeatherListFromTemperature(List<String> tempList){
      return tempList.stream()
             .map(temp -> new Weather("London", "Great Britain", LocalDateTime.now(),
                        temp, "-10", "7", "freezing"))
             .collect(Collectors.toList());
    }

}