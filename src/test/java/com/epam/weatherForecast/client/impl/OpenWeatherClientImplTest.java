package com.epam.weatherForecast.client.impl;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;
import java.net.URI;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@RestClientTest({OpenWeatherClientImpl.class})
@PropertySource("classpath:value.properties")
class OpenWeatherClientImplTest {

    @Autowired
    private  RestTemplate restTemplate;

    private MockRestServiceServer server;
    @Value("${openWeatherApiKey}")
    private String apiKey;
    private String city ="Astana";
    private String country ="Kazakhstan";
    private static final String CURRENT_WEATHER_URL =
        "http://api.openweathermap.org/data/2.5/weather?q={city},{country}&appid={key}&units=metric";
    private static final String TODAY_WEATHER_URL =
        "http://api.openweathermap.org/data/2.5/forecast?q={city},{country}&appid={key}&units=metric&cnt=8";


    @BeforeEach
    void setUp() {
        server = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void testUrlForGettingCurrentWeatherShouldRespondWithSuccess() {
        URI url = new UriTemplate(CURRENT_WEATHER_URL)
            .expand(city, country, apiKey);
        server.expect(requestTo(url))
            .andRespond(withStatus(HttpStatus.OK));
    }

    @Test
    void testUrlForGettingTodayWeatherShouldRespondWithSuccess() {
        URI url = new UriTemplate(TODAY_WEATHER_URL)
            .expand(city, country, apiKey);
        server.expect(requestTo(url))
            .andRespond(withStatus(HttpStatus.OK));
    }

    @AfterEach
    void resetMockServer(){
        server.reset();
    }
}