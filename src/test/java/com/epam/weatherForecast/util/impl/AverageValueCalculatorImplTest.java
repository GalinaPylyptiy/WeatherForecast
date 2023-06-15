package com.epam.weatherForecast.util.impl;

import com.epam.weatherForecast.model.Weather;
import com.epam.weatherForecast.util.AverageValueCalculator;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.List;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class AverageValueCalculatorImplTest {

    private AverageValueCalculator calculator =  new AverageValueCalculatorImpl();

    private List<Weather> getWeatherList(){
        Weather weather1 = new Weather("London", "Great Britain", LocalDateTime.now(),
                "-4", "-11", "5", "snowy");
        Weather weather2 = new Weather("London", "Great Britain", LocalDateTime.now(),
                "-7", "-10", "7", "freezing");
        return List.of(weather1, weather2);
    }

    @Test
    void shouldCalculateAverageTemperatureAndFeelsLikeAndReturnNegativeResult() {
        int averageTemp = calculator.calculateAverageValue(getWeatherList(), Weather::getTemperature);
        int averageFeelsLike = calculator.calculateAverageValue(getWeatherList(), Weather::getFeelsLike);
        assertThat(averageTemp).isNegative();
        assertThat(averageFeelsLike).isNegative();
    }

    @Test
    void shouldCalculateAverageWindSpeed(){
        int averageWindSpeed = calculator.calculateAverageValue(getWeatherList(), Weather::getWindSpeed);
        int result = 6;
        assertThat(averageWindSpeed).isEqualTo(result);
    }
}