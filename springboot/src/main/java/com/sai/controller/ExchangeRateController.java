package com.sai.controller;

import com.sai.model.ConversionRequest;
import com.sai.model.ConversionResponse;
import com.sai.service.ExchangeRateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class ExchangeRateController {
    private final ExchangeRateService exchangeRateService;

    public ExchangeRateController(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    @GetMapping("/rate")
    public ResponseEntity<Map<String, Double>> getRates(@RequestParam(defaultValue = "USD") String base) {
        Map<String, Double> rates = exchangeRateService.fetchExchangeRates(base);
        if (rates == null || rates.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok(rates);
    }

    @PostMapping("/conv")
    public ResponseEntity<ConversionResponse> convert(@RequestBody ConversionRequest request) {
        // Ensure currency codes are in uppercase
        String fromCurrency = request.getFrom().toUpperCase();
        String toCurrency = request.getTo().toUpperCase();

        // Fetch latest rates if cache is empty
        if (!exchangeRateService.isRatesCacheAvailable()) exchangeRateService.fetchExchangeRates("USD"); // Default base

        double rate = exchangeRateService.getExchangeRate(fromCurrency, toCurrency);
        double convertedAmount = request.getAmount() * rate;

        return ResponseEntity.ok(new ConversionResponse(fromCurrency, toCurrency, request.getAmount(), convertedAmount));
    }
}
