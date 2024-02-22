package ru.kpfu.itis.paramonov.controller;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kpfu.itis.paramonov.util.httpclient.HttpClient;

import java.util.HashMap;

@RestController
public class WeatherController {
    private final HttpClient httpClient;

    public WeatherController(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    private final String openWeatherApiURLDefault = "https://api.openweathermap.org/data/2.5/weather?q=kazan&appid=%s";

    @GetMapping("/weather")
    public String getWeather() {
        String openWeatherApiURL = String.format(openWeatherApiURLDefault, System.getenv("openweatherapi_key"));
        JSONObject weatherData = new JSONObject(httpClient.get(openWeatherApiURL, new HashMap<>()));
        return parseJSONWeather(weatherData);
    }

    private String parseJSONWeather(JSONObject json) {
        String weatherType = json.getJSONArray("weather").getJSONObject(0).getString("description");
        Double temperature = json.getJSONObject("main").getDouble("temp") - 273d;
        long humidity = json.getJSONObject("main").getLong("humidity");

        String weatherData = "Weather type: " + weatherType + ", " + "temperature: " + String.format("%.3f", temperature) +
                ", " + "humidity: " + humidity + "%";
        return weatherData;
    }
}
