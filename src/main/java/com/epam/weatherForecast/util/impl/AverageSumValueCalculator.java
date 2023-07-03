package com.epam.weatherForecast.util.impl;

import com.epam.weatherForecast.model.Weather;
import com.epam.weatherForecast.util.AverageValueCalculator;
import org.springframework.stereotype.Component;
import java.util.Collection;
import java.util.function.Function;

@Component
public class AverageSumValueCalculator implements AverageValueCalculator {

    @Override
    public int calculateAverageValue(Collection<Weather> weatherList, Function<Weather, String> valueFunction) {
        double sumTemp = weatherList.stream()
                .map(valueFunction)
                .mapToDouble(Double::parseDouble)
                .reduce(0, Double::sum);
        return (int) Math.ceil(sumTemp / weatherList.size());
    }
}
