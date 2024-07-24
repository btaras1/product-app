package com.product.app.service;

import com.product.app.exceptions.BadRequestException;
import com.product.app.gateway.HnbGateway;
import com.product.app.rest.dto.response.HnbApiResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static java.math.BigDecimal.ZERO;
import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class CurrencyService {

    static String USD_CURRENCY = "USD";
    static String FIXED_USD_EXCHANGE_RATE = "1.086000";

    HnbGateway hnbGateway;

    public BigDecimal convertEurToUsd(BigDecimal priceEur) {
        validatePriceEur(priceEur);
        return priceEur.multiply(getCurrentUsdExchangeRate());
    }

    private void validatePriceEur(BigDecimal priceEur) {
        if (priceEur == null || priceEur.compareTo(ZERO) < 0)  {
            throw new BadRequestException("EUR price of a product must be supplied and greater than zero.");
        }
    }

    private BigDecimal getCurrentUsdExchangeRate() {
        try {
            HnbApiResponseDto response = hnbGateway.getLatestExchangeRate(USD_CURRENCY);

            String exchangeRate = handleExchangeRateFromResponse(response.getMiddleExchangeRate());

            return new BigDecimal(exchangeRate);
        } catch (Exception ex) {
            log.warn(ex.getMessage());
            return new BigDecimal(FIXED_USD_EXCHANGE_RATE);
        }

    }

    private String handleExchangeRateFromResponse(String exchangeRate) {
        return exchangeRate.replace(",", ".");
    }
}
