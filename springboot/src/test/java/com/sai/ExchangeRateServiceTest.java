package com.sai;

import com.sai.service.ExchangeRateService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class ExchangeRateServiceTest {
    @MockBean
    private WebClient.Builder webClientBuilder;

    @Autowired
    private ExchangeRateService exchangeRateService;

    @Test
    public void testFetchExchangeRates() {
        Map<String, Double> rates = exchangeRateService.fetchExchangeRates("USD");
        assertNotNull(rates);
    }

    @Test
    public void testConvertCurrency() {
        double rate = exchangeRateService.getExchangeRate("USD", "EUR");
        assertTrue(rate > 0);
    }
}
