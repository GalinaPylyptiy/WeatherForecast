package com.epam.weatherForecast.util;

import com.epam.weatherForecast.model.Weather;

import java.util.Collection;
import java.util.function.Function;

public interface AverageValueCalculator {

    int calculateAverageValue(Collection<Weather> weatherList, Function<Weather, String> valueFunction);
}
