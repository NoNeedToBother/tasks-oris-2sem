package ru.kpfu.itis.paramonov.controller;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kpfu.itis.paramonov.util.httpclient.HttpClient;

import java.util.HashMap;

@RestController
public class CurrencyController {
    private final HttpClient httpClient;

    public CurrencyController(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    private final String currencyApiKeyDefault = "https://api.currencyapi.com/v3/latest?apikey=%s&currencies=RUB&base_currency=%s";

    @GetMapping("/currency")
    public String getCurrencyRate() {
        String currencyApiKey = System.getenv("currencyapi_key");
        String USDRate = getRateInfo(currencyApiKey, "USD");
        String EURRate = getRateInfo(currencyApiKey, "EUR");

        return USDRate + ", " + EURRate;
    }

    public String getRateInfo(String apiKey, String currency) {
        String currencyRateRequest = String.format(currencyApiKeyDefault, apiKey, currency);
        String currencyRate = httpClient.get(currencyRateRequest, new HashMap<>());

        JSONObject jsonCurrency = new JSONObject(currencyRate);
        Double exchangeRate = jsonCurrency.getJSONObject("data").getJSONObject("RUB").getDouble("value");
        return String.format(String.format("1 %s = %.3f RUB", currency, exchangeRate));
    }
}
