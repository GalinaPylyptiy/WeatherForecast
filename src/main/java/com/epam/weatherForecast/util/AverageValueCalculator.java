package com.epam.weatherForecast.util;

import com.epam.weatherForecast.model.Weather;

import java.util.Collection;
import java.util.function.Function;


// TODO realize  new implementation of average calculator
/**
 *  1. use TDD (test the first)
 *  2. use math not simple method
 *  3. then write code
 *  4. 1 and 3 is loop
 */
public interface AverageValueCalculator {

    int calculateAverageValue(Collection<Weather> weatherList, Function<Weather, String> valueFunction);
}
