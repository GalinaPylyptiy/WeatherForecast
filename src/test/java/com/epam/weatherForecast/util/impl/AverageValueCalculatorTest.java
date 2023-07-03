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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class AverageValueCalculatorTest {

    private AverageValueCalculator calculator = new AverageSumValueCalculator();

    private static Stream<Arguments> provideTemperatureDataForAvgTempCalculate(){
        return Stream.of(

               Arguments.of("0", "-1", "0"),
               Arguments.of("0", "1", "1"),
               Arguments.of("28", "30", "29"),
               Arguments.of("15", "12", "14")
        );
    }

    private static Stream<Arguments> provideNegativeTemperatureDataForAvgTempCalculate(){
        return Stream.of(
                Arguments.of("-6", "-8"),
                Arguments.of("-2", "-1"),
                Arguments.of("-15", "-16"),
                Arguments.of("-3", "-6")
        );
    }

    @ParameterizedTest
    @DisplayName("Must calculate average value in temperature")
    @MethodSource("provideTemperatureDataForAvgTempCalculate")
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
    @MethodSource("provideNegativeTemperatureDataForAvgTempCalculate")
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

}