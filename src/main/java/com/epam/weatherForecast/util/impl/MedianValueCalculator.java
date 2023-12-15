package com.epam.weatherForecast.util.impl;
import com.epam.weatherForecast.model.Weather;
import com.epam.weatherForecast.util.AverageValueCalculator;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;


public class MedianValueCalculator implements AverageValueCalculator {

    @Override
    public int calculateAverageValue(Collection<Weather> weatherList, Function<Weather, String> valueFunction) {
        List<Double> valueList = getWeatherValueList(weatherList, valueFunction);
        if (valueList.size() % 2 == 0)
            return (int) Math.round(calculateAvgCenterValueInEvenSizedList(valueList));
        return getValueFromOddSizedWeatherList(valueList);
    }

    private List<Double> getWeatherValueList(Collection<Weather> weatherList, Function<Weather, String> valueFunction) {
        return weatherList.stream()
                .map(valueFunction)
                .map(Double::parseDouble)
                .sorted()
                .collect(Collectors.toList());
    }

    private double calculateAvgCenterValueInEvenSizedList(List<Double> valueList) {
        int listSize = valueList.size();
        int listCenter = valueList.size() / 2;
        double value1 = valueList.get((listSize - 1) / 2);
        double value2 = valueList.get(listCenter);
        return (value1 + value2) / 2;

    }

    private int getValueFromOddSizedWeatherList(List<Double> valueList) {
        int listCenter = valueList.size() / 2;
        double centerElement = valueList.get(listCenter);
        return (int) Math.round(centerElement);
    }
}
