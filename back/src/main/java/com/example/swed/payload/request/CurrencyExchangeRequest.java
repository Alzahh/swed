package com.example.swed.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CurrencyExchangeRequest {
    private String baseCurrency;
    private String newCurrency;
    private Double baseAmount;
}
