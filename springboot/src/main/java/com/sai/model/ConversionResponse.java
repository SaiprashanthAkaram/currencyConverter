package com.sai.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ConversionResponse {

    private String from;
    private String to;
    private double originalAmount;
    private double convertedAmount;

    // Corrected constructor that initializes fields
    public ConversionResponse(String from, String to, double originalAmount, double convertedAmount) {
        this.from = from;
        this.to = to;
        this.originalAmount = originalAmount;
        this.convertedAmount = convertedAmount;
    }
}


