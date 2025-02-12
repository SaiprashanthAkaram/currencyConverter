package com.sai.service;

import com.sai.exception.CurrencyNotFoundException;
import com.sai.model.ExchangeRateResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Service
public class ExchangeRateService {

    private final WebClient webClient;

    @Value("${convert.url}")
    private String apiUrl;

    @Value("${convert.key}")
    private String apiKey;

    private Map<String, Double> ratesCache = new HashMap<>();

    public ExchangeRateService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public Map<String, Double> fetchExchangeRates(String base) {
        String url = String.format("%s/latest?base=%s&apikey=%s", apiUrl, base, apiKey);

        ExchangeRateResponse response = webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(ExchangeRateResponse.class)
                .onErrorResume(e -> {
                    System.err.println("Error fetching exchange rates: " + e.getMessage());
                    return Mono.empty();
                })
                .block();

        if (response != null && response.getRates() != null) {
            ratesCache = response.getRates();
        }

        return ratesCache;
    }

    public Double getExchangeRate(String from, String to) {
        if (!ratesCache.containsKey(from) || !ratesCache.containsKey(to)) {
            throw new CurrencyNotFoundException("Invalid currency code: " + from + " or " + to);
        }
        return ratesCache.get(to) / ratesCache.get(from);
    }

    public boolean isRatesCacheAvailable() {
        return !ratesCache.isEmpty();
    }
}
