package com.example.swed.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CurrencyExchangeResponse {
    private Double newAmount;
}
